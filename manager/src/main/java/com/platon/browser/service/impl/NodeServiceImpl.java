package com.platon.browser.service.impl;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeServiceImpl implements NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Value("${platon.image.server.url}")
    private String imageServerUrl;

    @Override
    public List<NodePushItem> getNodeInfoList() {
        List<NodeRanking> nodeList = nodeRankingMapper.selectByExample(new NodeRankingExample());
        List<NodePushItem> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodePushItem nodeInfo = new NodePushItem();
            nodeInfoList.add(nodeInfo);
            BeanUtils.copyProperties(node,nodeInfo);
        });
        return nodeInfoList;
    }

    /**
     * 对统计信息按StatisticsEnum分类
     * @param statisticsList
     * @return
     */
    private Map<StatisticsEnum,Map<String,String>> classifyStatistic(List<Statistics> statisticsList){
        Map<StatisticsEnum,Map<String,String>> map = new HashMap<>();
        //   |-- 按节点分类统计指标信息
        // 累计分红
        Map<String,String> rewardAmountMap = new HashMap<>();
        map.put(StatisticsEnum.reward_amount,rewardAmountMap);
        // 累计收益
        Map<String,String> profitAmountMap = new HashMap<>();
        map.put(StatisticsEnum.profit_amount,profitAmountMap);
        // 节点验证次数
        Map<String,String> verifyCountMap = new HashMap<>();
        map.put(StatisticsEnum.verify_count,verifyCountMap);
        // 已出块数
        Map<String,String> blockCountMap = new HashMap<>();
        map.put(StatisticsEnum.block_count,blockCountMap);
        // 区块累计奖励
        Map<String,String> blockRewardMap = new HashMap<>();
        map.put(StatisticsEnum.block_reward,blockRewardMap);
        statisticsList.forEach(statistics -> {
            try {
                StatisticsEnum senum = StatisticsEnum.getEnum(statistics.getType());
                switch (senum){
                    case reward_amount:
                        rewardAmountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case profit_amount:
                        profitAmountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case verify_count:
                        verifyCountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case block_count:
                        blockCountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case block_reward:
                        blockRewardMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                }
            }catch (Exception e){
                // 枚举获取出现异常
                logger.error("统计类型异常：{}",statistics.getType());
                return;
            }
        });
        return map;
    }

    @Override
    public List<NodeListItem> getNodeItemList(NodeListReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getKeyword())){
            // 根据账户名称查询
            criteria.andNameEqualTo(req.getKeyword());
        }
        condition.setOrderByClause("ranking asc");
        List<NodeRanking> list = nodeRankingMapper.selectByExample(condition);

        List<NodeListItem> itemList = new LinkedList<>();

        if(list.size()==0){
            return itemList;
        }

        // 批量查询节点的统计信息
        List<String> nodeIdList = new ArrayList<>();
        list.forEach(node->nodeIdList.add(node.getNodeId()));
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid())
                .andTypeEqualTo(StatisticsEnum.block_count.name())
                .andNodeIdIn(nodeIdList);
        List<Statistics> statisticsList = statisticsMapper.selectByExample(statisticsExample);
        Map<StatisticsEnum,Map<String,String>> statisticsMap = classifyStatistic(statisticsList);
        Map<String,String> blockCountMap = statisticsMap.get(StatisticsEnum.block_count);

        list.forEach(node -> {
            NodeListItem bean = new NodeListItem();
            BeanUtils.copyProperties(node,bean);
            try {
                CityResponse response = GeoUtil.getResponse(node.getIp());
                Country country = response.getCountry();
                bean.setCountryCode(country.getIsoCode());
                if(StringUtils.isNotBlank(country.getName())){
                    bean.setLocation(country.getName());
                }
                City city = response.getCity();
                if(StringUtils.isNotBlank(city.getName())){
                    bean.setLocation(bean.getLocation()+" "+city.getName());
                }
            }catch (Exception e){
                bean.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
            }

            // 设置统计信息
            String blockCountStr = blockCountMap.get(node.getId());
            int blockCount = 0;
            if(StringUtils.isNotBlank(blockCountStr)){
                blockCount = Integer.valueOf(blockCountStr);
            }
            bean.setBlockCount(blockCount);

            // 设置logo url
            bean.setLogo(imageServerUrl+node.getUrl());

            itemList.add(bean);
        });
        return itemList;
    }

    @Override
    public NodeDetail getNodeDetail(NodeDetailReq req, boolean byName) {
        NodeRankingExample condition = new NodeRankingExample();
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(byName){
            criteria.andNameEqualTo(req.getId());
        }else{
           // criteria.andIdEqualTo(req.getId().toString());
        }
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        if (nodes.size()>1){
            logger.error("duplicate node: node {} {}",byName?"name":"id",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_DUPLICATE));
        }
        if(nodes.size()==0){
            logger.error("invalid node {} {}",byName?"name":"id",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }

        NodeDetail nodeDetail = new NodeDetail();
        NodeRanking currentNode = nodes.get(0);
        BeanUtils.copyProperties(currentNode,nodeDetail);
        nodeDetail.setJoinTime(currentNode.getJoinTime().getTime());
        nodeDetail.setNodeUrl("http://"+currentNode.getIp()+":"+currentNode.getPort());

        // 设置统计信息
        // 批量查询节点的统计信息
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid()).andNodeIdEqualTo(currentNode.getNodeId());
        List<Statistics> statisticsList = statisticsMapper.selectByExample(statisticsExample);
        Map<StatisticsEnum,Map<String,String>> statisticsMap = classifyStatistic(statisticsList);
        // 累计分红
        Map<String,String> rewardAmountMap = statisticsMap.get(StatisticsEnum.reward_amount);
        // 累计收益
        Map<String,String> profitAmountMap = statisticsMap.get(StatisticsEnum.profit_amount);
        // 节点验证次数
        Map<String,String> verifyCountMap = statisticsMap.get(StatisticsEnum.verify_count);
        // 已出块数
        Map<String,String> blockCountMap = statisticsMap.get(StatisticsEnum.block_count);

        String blockCountStr = blockCountMap.get(currentNode.getId());
        if(StringUtils.isNotBlank(blockCountStr)){
            nodeDetail.setBlockCount(Integer.valueOf(blockCountStr));
        }else{
            nodeDetail.setBlockCount(0);
        }

        String rewardAmountStr = rewardAmountMap.get(currentNode.getId());
        nodeDetail.setRewardAmount(StringUtils.isBlank(rewardAmountStr)?"0":rewardAmountStr);
        String profitAmountStr = profitAmountMap.get(currentNode.getId());
        nodeDetail.setProfitAmount(StringUtils.isBlank(profitAmountStr)?"0":profitAmountStr);
        String verifyCountStr = verifyCountMap.get(currentNode.getId());
        if(StringUtils.isNotBlank(verifyCountStr)){
            nodeDetail.setVerifyCount(Integer.valueOf(verifyCountStr));
        }else{
            nodeDetail.setVerifyCount(0);
        }


        // 设置地理位置信息
        try {
            CityResponse response = GeoUtil.getResponse(currentNode.getIp());
            Country country = response.getCountry();
            if(StringUtils.isNotBlank(country.getName())){
                nodeDetail.setLocation(country.getName());
            }
            City city = response.getCity();
            if(StringUtils.isNotBlank(city.getName())){
                nodeDetail.setLocation(nodeDetail.getLocation()+" "+city.getName());
            }
        }catch (Exception e){
            nodeDetail.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
        }

        // 公钥就是节点ID
        nodeDetail.setPublicKey(currentNode.getNodeId());
        // 钱包就是address
        nodeDetail.setWallet(currentNode.getAddress());

        // 设置logo url
        nodeDetail.setLogo(imageServerUrl+currentNode.getUrl());

        return nodeDetail;
    }

    @Override
    public List<BlockListItem> getBlockList(BlockListReq req) {
        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        List<Block> blocks = blockService.getBlockList(downloadReq);
        List<BlockListItem> blockItemList = new LinkedList<>();
        blocks.forEach(block -> {
            BlockListItem bean = new BlockListItem();
            BeanUtils.copyProperties(block,bean);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setHeight(block.getNumber());
            bean.setTransaction(block.getTransactionNumber());
            blockItemList.add(bean);
        });
        return blockItemList;
    }


}
