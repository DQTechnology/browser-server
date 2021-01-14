package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.response.token.QueryTokenHolderListResp;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
import com.platon.browser.service.OldErc20TxService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 合约内部交易模块Controller
 *
 * @author AgentRJ
 * @create 2020-09-23 15:04
 */
@Slf4j
@RestController
public class OldErc20TxController {

    @Resource
    private OldErc20TxService oldErc20TxService;
    @Resource
    private DownFileCommon downFileCommon;
    @Resource
    private CommonMethod commonMethod;
    @Resource
    private I18nUtil i18n;

    @PostMapping("token/tokenTransferList")
    public Mono<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@Valid @RequestBody QueryTokenTransferRecordListReq req) {
        return Mono.just(oldErc20TxService.queryTokenRecordList(req));
    }

    @GetMapping("token/exportTokenTransferList")
    public void exportTokenTransferList(
            @RequestParam(value = "address",required = false) String address,
            @RequestParam(value = "contract",required = false) String contract,
            @RequestParam(value = "date", required = true) Long date,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = oldErc20TxService.exportTokenTransferList(address, contract, date, local, timeZone, token, response);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @PostMapping("token/tokenHolderList")
    public Mono<RespPage<QueryTokenHolderListResp>> tokenHolderList(@Valid @RequestBody QueryTokenHolderListReq req) {
        return Mono.just(oldErc20TxService.tokenHolderList(req));
    }

    @GetMapping("token/exportTokenHolderList")
    public void exportTokenHolderList(
            @RequestParam(value = "contract",required = true) String contract,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = oldErc20TxService.exportTokenHolderList(contract, local, timeZone, token, response);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @PostMapping("token/holderTokenList")
    public Mono<RespPage<QueryHolderTokenListResp>> holderTokenList(@Valid @RequestBody QueryHolderTokenListReq req) {
        return Mono.just(oldErc20TxService.holderTokenList(req));
    }

    @GetMapping("token/exportHolderTokenList")
    public void exportHolderTokenList(
            @RequestParam(value = "address",required = true) String address,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = oldErc20TxService.exportHolderTokenList(address, local, timeZone, token, response);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }
}