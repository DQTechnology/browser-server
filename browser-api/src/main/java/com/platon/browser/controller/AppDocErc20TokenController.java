package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.Erc20TokenService;
import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;
import com.platon.browser.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

@RestController
@Slf4j
public class AppDocErc20TokenController implements AppDocErc20Token {

    @Autowired
    private Erc20TokenService erc20TokenService;

    @Autowired
    private I18nUtil i18n;

    @Override
    public WebAsyncTask<BaseResp<QueryTokenDetailResp>> tokenDetail(@Valid QueryTokenDetailReq req) {
        WebAsyncTask<BaseResp<QueryTokenDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            QueryTokenDetailResp queryDetailResp = erc20TokenService.queryTokenDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryDetailResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<QueryTokenListResp>> tokenList(@Valid QueryTokenListReq req) {
        WebAsyncTask<RespPage<QueryTokenListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            return erc20TokenService.queryTokenList(req);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }
}
