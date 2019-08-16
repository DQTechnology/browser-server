package com.platon.browser.dto.transaction;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.util.EnergonUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class TransactionPushItem {
    private String txHash;
    private String from;
    private String to;
    private String value;
    private Long blockHeight;
    private Integer transactionIndex;
    private Long timestamp;
    private String txType;
    private String receiveType;

    public void init( Transaction initData){
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setBlockHeight(initData.getBlockNumber());
        BigDecimal v = Convert.fromVon(initData.getValue(), Convert.Unit.LAT);
        this.setValue(EnergonUtil.format(v));
    }
}