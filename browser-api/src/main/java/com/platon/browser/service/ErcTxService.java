package com.platon.browser.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.cache.TokenTransferRecordCacheDto;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.response.token.QueryTokenHolderListResp;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
import com.platon.browser.service.elasticsearch.AbstractEsRepository;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.ConvertUtil;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.I18nUtil;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合约内部转账交易记录
 *
 * @author AgentRJ
 * @create 2020-09-23 16:08
 */
@Service
@Slf4j
public class ErcTxService {

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private I18nUtil i18n;

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private NetworkStatMapper networkStatMapper;

    /**
     * 默认精度
     */
    private static final Integer decimal = 6;

    public RespPage<QueryTokenTransferRecordListResp> token20TransferList(QueryTokenTransferRecordListReq req) {
        return this.getList(req, esErc20TxRepository, ErcTypeEnum.ERC20);
    }

    public RespPage<QueryTokenTransferRecordListResp> token721TransferList(QueryTokenTransferRecordListReq req) {
        return this.getList(req, esErc721TxRepository, ErcTypeEnum.ERC721);
    }

    private RespPage<QueryTokenTransferRecordListResp> getList(QueryTokenTransferRecordListReq req, AbstractEsRepository repository, ErcTypeEnum typeEnum) {
        if (log.isDebugEnabled()) {
            log.debug("~ queryTokenRecordList, params: " + JSON.toJSONString(req));
        }

        // logic:
        // 1、合约内部交易列表中，数据存储于ES，列表的获取走ES获取
        // 2、所有查询直接走ES，不进行DB检索
        RespPage<QueryTokenTransferRecordListResp> result = new RespPage<>();

        List<ErcTx> records;
        long totalCount = 0;
        long displayTotalCount = 0;
        if (StringUtils.isEmpty(req.getContract()) && StringUtils.isEmpty(req.getAddress()) && StringUtils.isEmpty(req.getTokenId())) {
            // 仅分页查询，直接走缓存
            TokenTransferRecordCacheDto tokenTransferRecordCacheDto = statisticCacheService.getTokenTransferCache(req.getPageNo(), req.getPageSize(), typeEnum);
            records = tokenTransferRecordCacheDto.getTransferRecordList();
            totalCount = tokenTransferRecordCacheDto.getPage().getTotalCount();
            displayTotalCount = tokenTransferRecordCacheDto.getPage().getTotalPages();
        } else {
            // construct of params
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            ESResult<ErcTx> queryResultFromES = new ESResult<>();

            // condition: txHash/contract/txFrom/transferTo
            if (StringUtils.isNotEmpty(req.getContract())) {
                constructor.must(new ESQueryBuilders().terms("contract", Collections.singletonList(req.getContract())));
            }
            if (StrUtil.isNotBlank(req.getTokenId())) {
                constructor.must(new ESQueryBuilders().term("value", req.getTokenId()));
            }
            if (StringUtils.isNotEmpty(req.getAddress())) {
                constructor.buildMust(new BoolQueryBuilder()
                        .should(QueryBuilders.termQuery("from", req.getAddress()))
                        .should(QueryBuilders.termQuery("to", req.getAddress())));
            }
            if (StringUtils.isNotEmpty(req.getTxHash())) {
                constructor.must(new ESQueryBuilders().term("hash", req.getTxHash()));
            }
            // Set sort field
            constructor.setDesc("seq");
            // response filed to show.
            constructor.setResult(new String[]{"seq", "hash", "bn", "from", "contract",
                    "to", "value", "decimal", "name", "symbol", "result", "bTime"});
            try {
                queryResultFromES = repository.search(constructor, ErcTx.class,
                        req.getPageNo(), req.getPageSize());
                totalCount = queryResultFromES.getTotal();
                displayTotalCount = queryResultFromES.getTotal();
            } catch (Exception e) {
                log.error("检索代币交易列表失败", e);
                return result;
            }

            records = queryResultFromES.getRsData();
            if (null == records || records.size() == 0) {
                log.debug("未检索到有效数据，参数：" + JSON.toJSONString(req));
                return result;
            }
        }

        List<QueryTokenTransferRecordListResp> recordListResp = records.parallelStream()
                .filter(p -> p != null && p.getDecimal() != null)
                .map(p -> this.toQueryTokenTransferRecordListResp(req.getAddress(), p)).collect(Collectors.toList());
        result.init(recordListResp, totalCount, displayTotalCount, totalCount / req.getPageSize() + 1);
        if (StrUtil.isEmpty(req.getContract())) {
            // 从数据库查询网络表取最新的token交易数
            List<NetworkStat> networkStatList = networkStatMapper.selectByExample(null);
            if (networkStatList != null && !networkStatList.isEmpty()) {
                NetworkStat networkStat = networkStatList.get(0);
                totalCount = typeEnum == ErcTypeEnum.ERC20 ? networkStat.getErc20TxQty() : networkStat.getErc721TxQty();
                result.setTotalCount(totalCount);
                result.setDisplayTotalCount(totalCount);
            }
        }
        return result;
    }

    public AccountDownload exportToken20TransferList(String address, String contract, Long date, String local, String timeZone) {
        return this.exportTokenTransferList(address, contract, date, local, timeZone, esErc20TxRepository, null);
    }

    public AccountDownload exportToken721TransferList(String address, String contract, Long date, String local, String timeZone, String tokenId) {
        return this.exportTokenTransferList(address, contract, date, local, timeZone, esErc721TxRepository, tokenId);
    }

    public AccountDownload exportTokenTransferList(String address, String contract, Long date, String local, String timeZone
            , AbstractEsRepository repository, String tokenId) {
        AccountDownload accountDownload = new AccountDownload();
        if (StringUtils.isBlank(address) && StringUtils.isBlank(contract)) {
            return accountDownload;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        log.info("导出地址交易列表数据起始日期：{},结束日期：{}", dateFormat.format(date), dateFormat.format(currentServerTime));

        // construct of params
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().range("bTime", new Date(date).getTime(), currentServerTime.getTime()));
        ESResult<ErcTx> queryResultFromES = new ESResult<>();
        // condition: txHash/contract/txFrom/transferTo
        if (StringUtils.isNotBlank(contract)) {
            constructor.must(new ESQueryBuilders().term("contract", contract));
        }
        if (StrUtil.isNotBlank(tokenId)) {
            constructor.must(new ESQueryBuilders().term("value", tokenId));
        }
        if (StringUtils.isNotBlank(address)) {
            constructor.buildMust(new BoolQueryBuilder()
                    .should(QueryBuilders.termQuery("from", address))
                    .should(QueryBuilders.termQuery("tto", address)));
        }
        // Set sort field
        constructor.setDesc("seq");
        // response filed to show.
        constructor.setResult(new String[]{"seq", "hash", "bn", "from", "contract",
                "to", "value", "decimal", "name", "symbol", "result", "bTime"});
        try {
            queryResultFromES = repository.search(constructor, ErcTx.class,
                    1, 30000);
        } catch (Exception e) {
            log.error("检索代币交易列表失败", e);
            return accountDownload;
        }
        List<Object[]> rows = new ArrayList<>();
        queryResultFromES.getRsData().stream().forEach(esTokenTransferRecord -> {
            if (StringUtils.isNotBlank(address)) {
                boolean toIsAddress = address.equals(esTokenTransferRecord.getTo());
                String valueIn = toIsAddress ? esTokenTransferRecord.getValue() : "0";
                String valueOut = !toIsAddress ? esTokenTransferRecord.getValue() : "0";
                Object[] row = {esTokenTransferRecord.getHash(),
                        DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(), "0", timeZone),
                        esTokenTransferRecord.getFrom(), esTokenTransferRecord.getTo(),
                        /** 数值von转换成lat，并保留十八位精确度 */
                        HexUtil.append(ConvertUtil.convertByFactor(new BigDecimal(valueIn), esTokenTransferRecord.getDecimal()).toString()),
                        HexUtil.append(ConvertUtil.convertByFactor(new BigDecimal(valueOut), esTokenTransferRecord.getDecimal()).toString()),
                        esTokenTransferRecord.getSymbol()
                };
                rows.add(row);
            } else if (StringUtils.isNotBlank(contract)) {
                Object[] row = {esTokenTransferRecord.getHash(),
                        DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(), "0", timeZone),
                        esTokenTransferRecord.getFrom(), esTokenTransferRecord.getTo(),
                        /** 数值von转换成lat，并保留十八位精确度 */
                        HexUtil.append(ConvertUtil.convertByFactor(new BigDecimal(esTokenTransferRecord.getValue()), esTokenTransferRecord.getDecimal()).toString()),
                        esTokenTransferRecord.getSymbol()
                };
                rows.add(row);
            }
        });
        String[] headers = {};
        if (StringUtils.isNotBlank(address)) {
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_VALUE_IN, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_VALUE_OUT, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local)};
        } else if (StringUtils.isNotBlank(contract)) {
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE, local),
                    this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local)};
        }
        return this.downFileCommon.writeDate("InnerTransaction-" + address + "-" + date + ".CSV", rows, headers);
    }

    public RespPage<QueryTokenHolderListResp> tokenHolderList(QueryTokenHolderListReq req) {
        if (log.isDebugEnabled()) {
            log.debug("~ tokenHolderList, params: " + JSON.toJSONString(req));
        }
        RespPage<QueryTokenHolderListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<CustomTokenHolder> ids = this.customTokenHolderMapper.selectListByParams(req.getContract(), null, null);
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        List<QueryTokenHolderListResp> respList = new ArrayList<>();
        ids.getResult().forEach(tokenHolder -> {
            QueryTokenHolderListResp resp = new QueryTokenHolderListResp();
            resp.setAddress(tokenHolder.getAddress());
            BigDecimal originBalance = getAddressBalance(tokenHolder);
            originBalance = (originBalance == null) ? BigDecimal.ZERO : originBalance;
            if (tokenHolder.getDecimal() != null) {
                //金额转换成对应的值
                BigDecimal balance = ConvertUtil.convertByFactor(originBalance, tokenHolder.getDecimal());
                resp.setBalance(balance);
                //计算总供应量
                BigDecimal originTotalSupply = tokenHolder.getTotalSupply();
                originTotalSupply = (originTotalSupply == null) ? BigDecimal.ZERO : originTotalSupply;
                BigDecimal totalSupply = ConvertUtil.convertByFactor(originTotalSupply, tokenHolder.getDecimal());
                if (totalSupply.compareTo(BigDecimal.ZERO) > 0) {
                    // 总供应量大于0, 使用实际的余额除以总供应量
                    resp.setPercent(
                            balance
                                    .divide(totalSupply, decimal, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(decimal, RoundingMode.HALF_UP)
                                    .stripTrailingZeros()
                                    .toPlainString() + "%"
                    );
                } else {
                    // 总供应量小于等于0，则占比设置为0%
                    resp.setPercent("0.0000%");
                }
            } else {
                resp.setBalance(originBalance);
                resp.setPercent("0.0000%");
            }
            respList.add(resp);
        });
        result.init(ids, respList);
        return result;
    }

    public RespPage<QueryHolderTokenListResp> holderTokenList(@Valid QueryHolderTokenListReq req) {
        if (log.isDebugEnabled()) {
            log.debug("~ tokenHolderList, params: " + JSON.toJSONString(req));
        }
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        RespPage<QueryHolderTokenListResp> result = new RespPage<>();
        Page<CustomTokenHolder> ids = new Page<>();
        if ("erc20".equalsIgnoreCase(req.getType())) {
            ids = this.customTokenHolderMapper.selectListByParams(null, req.getAddress(), req.getType());
        }
        if ("erc721".equalsIgnoreCase(req.getType())) {
            ids = customTokenHolderMapper.selectListByERC721(null, req.getAddress());
        }
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        List<QueryHolderTokenListResp> listResps = new ArrayList<>();
        ids.stream().forEach(tokenHolder -> {
            QueryHolderTokenListResp queryHolderTokenListResp = new QueryHolderTokenListResp();
            BeanUtils.copyProperties(tokenHolder, queryHolderTokenListResp);
            queryHolderTokenListResp.setContract(tokenHolder.getTokenAddress());

            QueryTokenTransferRecordListReq queryTokenTransferRecordListReq = new QueryTokenTransferRecordListReq();
            queryTokenTransferRecordListReq.setPageNo(1);
            queryTokenTransferRecordListReq.setPageSize(30000);
            queryTokenTransferRecordListReq.setContract(tokenHolder.getTokenAddress());
            queryTokenTransferRecordListReq.setAddress(req.getAddress());
            RespPage<QueryTokenTransferRecordListResp> list = new RespPage<>();
            if ("erc20".equalsIgnoreCase(req.getType())) {
                list = token20TransferList(queryTokenTransferRecordListReq);
            }
            if ("erc721".equalsIgnoreCase(req.getType())) {
                list = token721TransferList(queryTokenTransferRecordListReq);

            }
            queryHolderTokenListResp.setTxCount(Convert.toInt(list.getTotalCount()));

            BigDecimal balance = this.getAddressBalance(tokenHolder);
            //金额转换成对应的值
            if (null != balance) {
                BigDecimal actualTransferValue = ConvertUtil.convertByFactor(balance, tokenHolder.getDecimal());
                queryHolderTokenListResp.setBalance(actualTransferValue);
            } else {
                queryHolderTokenListResp.setBalance(BigDecimal.ZERO);
            }
            listResps.add(queryHolderTokenListResp);
        });
        result.init(ids, listResps);
        return result;
    }

    public AccountDownload exportTokenHolderList(String contract, String local, String timeZone) {
        PageHelper.startPage(1, 30000);
        Page<CustomTokenHolder> rs = this.customTokenHolderMapper.selectListByParams(contract, null, null);
        List<Object[]> rows = new ArrayList<>();
        rs.stream().forEach(customTokenHolder -> {
            BigDecimal balance = this.getAddressBalance(customTokenHolder);
            Object[] row = {customTokenHolder.getAddress(), HexUtil.append(ConvertUtil.convertByFactor(balance,
                    customTokenHolder.getDecimal()).toString()),
                    balance.divide(customTokenHolder.getTotalSupply())
                            .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%"
            };
            rows.add(row);
        });
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
        return this.downFileCommon.writeDate("TokenHolder-" + contract + "-" + new Date().getTime() + ".CSV", rows, headers);
    }

    public AccountDownload exportHolderTokenList(String address, String local, String timeZone, String type) {

        PageHelper.startPage(1, 30000);

        String[] tokenType = {"erc20", "erc721"};
        if (!ArrayUtil.contains(tokenType, type)) {
            type = null;
        }
        Page<CustomTokenHolder> rs = this.customTokenHolderMapper.selectListByParams(null, address, type);

        List<Object[]> rows = new ArrayList<>();
        rs.stream().forEach(customTokenHolder -> {
            BigDecimal balance = this.getAddressBalance(customTokenHolder);
            Object[] row = {customTokenHolder.getName(), customTokenHolder.getSymbol(),
                    HexUtil.append(ConvertUtil.convertByFactor(balance, customTokenHolder.getDecimal()).toString()),
                    customTokenHolder.getDecimal(), customTokenHolder.getTxCount(), customTokenHolder.getTokenAddress()
            };
            rows.add(row);
        });
        String[] headers = {
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_NAME, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_DECIMALS, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TXCOUNT, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT, local)
        };
        return this.downFileCommon.writeDate("HolderToken-" + address + "-" + new Date().getTime() + ".CSV", rows, headers);
    }

    public QueryTokenTransferRecordListResp toQueryTokenTransferRecordListResp(String address, ErcTx record) {
        QueryTokenTransferRecordListResp resp = QueryTokenTransferRecordListResp.builder()
                .seq(record.getSeq())
                .txHash(record.getHash()).blockNumber(record.getBn())
                .txFrom(record.getFrom()).contract(record.getContract())
                .transferTo(record.getTo()).name(record.getName())
                .decimal(record.getDecimal()).symbol(record.getSymbol())
                .tokenId(record.getValue())
//                .result(record.getResult())
                .value(new BigDecimal(record.getValue()))
                .blockTimestamp(record.getBTime()).systemTimestamp(new Date().getTime())
                .value(null == record.getValue() ? BigDecimal.ZERO : new BigDecimal(record.getValue()))
                .fromType(record.getFromType()).toType(record.getToType())
                .build();
        // Processing accuracy calculation.
        if (null != record.getValue()) {
            BigDecimal transferValue = new BigDecimal(record.getValue());
            BigDecimal actualTransferValue = ConvertUtil.convertByFactor(transferValue, record.getDecimal());
            resp.setTransferValue(actualTransferValue);
        } else {
            resp.setTransferValue(BigDecimal.ZERO);
        }
        // input or out
        if (null != address && address.equals(record.getFrom())) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.OUT.val());
        } else {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.INPUT.val());
        }
        if (null == address) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.NONE.val());
        }
        return resp;
    }

    private BigDecimal getAddressBalance(CustomTokenHolder tokenHolder) {
        //暂时由后台统计余额
        return tokenHolder.getBalance();
//        return this.ercService.getBalance(erc20TokenAddressRel.getContract(), erc20TokenAddressRel.getAddress());
    }

}
