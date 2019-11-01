package com.platon.browser.common.dto;

import com.platon.browser.client.result.Receipt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class CollectionTransaction extends Transaction {
    private CollectionTransaction(){}
    public static final CollectionTransaction newInstance(){
        CollectionTransaction transaction = new CollectionTransaction();
        Date date = new Date();
        transaction.setCreTime(date);
        transaction.setUpdTime(date);
        transaction.setCost(BigDecimal.ZERO);
        transaction.setGasLimit(BigDecimal.ZERO);
        transaction.setGasPrice(BigDecimal.ZERO);
        transaction.setGasUsed(BigDecimal.ZERO);
        transaction.setStatus(StatusEnum.FAILURE.code);
        transaction.setValue(BigDecimal.ZERO);
        transaction.setIndex(0);
        return transaction;
    }

    public CollectionTransaction updateWithBlock(CollectionBlock block){
        this.setTime(block.getTime());
        return this;
    }

    public CollectionTransaction updateWithRawTransaction(org.web3j.protocol.core.methods.response.Transaction transaction){
        this.setNum(transaction.getBlockNumber().longValue());
        this.setBHash(transaction.getBlockHash());
        this.setHash(transaction.getHash());
        this.setValue(new BigDecimal(transaction.getValue()));
        this.setIndex(transaction.getTransactionIndex().intValue());
        this.setGasPrice(new BigDecimal(transaction.getGasPrice()));
        this.setInput(transaction.getInput());
        this.setTo(transaction.getTo());
        this.setFrom(transaction.getFrom());
        this.setNonce(transaction.getNonce().toString());
        return this;
    }

    public CollectionTransaction updateWithReceipt(Receipt receipt){
        this.setGasUsed(new BigDecimal(receipt.getGasUsed()));
        this.setCost(this.getGasUsed().multiply(this.getGasPrice()));
        this.setFailReason(receipt.getFailReason());
        this.setStatus(receipt.getReceiptStatus());
        this.setSeq(this.getNum()*10000+this.getIndex());
        return this;
    }

    public enum TypeEnum {
        TRANSFER("0", "转账"),
        CONTRACT_CREATION("1","合约发布(合约创建)"),
        CONTRACT_EXECUTION("2","合约调用(合约执行)"),
        OTHERS("4","其他"),
        MPC("5","MPC交易"),
        CREATE_VALIDATOR("1000","发起质押(创建验证人)"),
        EDIT_VALIDATOR("1001","修改质押信息(编辑验证人)"),
        INCREASE_STAKING("1002","增持质押(增加自有质押)"),
        EXIT_VALIDATOR("1003","撤销质押(退出验证人)"),
        DELEGATE("1004","发起委托(委托)"),
        UN_DELEGATE("1005","减持/撤销委托(赎回委托)"),
        CREATE_PROPOSAL_TEXT("2000","提交文本提案(创建提案)"),
        CREATE_PROPOSAL_UPGRADE("2001","提交升级提案(创建提案)"),
        CREATE_PROPOSAL_PARAMETER("2002","提交参数提案(创建提案)"),
        CANCEL_PROPOSAL("2005","提交取消提案"),
        VOTING_PROPOSAL("2003","给提案投票(提案投票)"),
        DECLARE_VERSION("2004","版本声明"),
        REPORT_VALIDATOR("3000","举报多签(举报验证人)"),
        CREATE_RESTRICTING("4000","创建锁仓计划(创建锁仓)"),
        DUPLICATE_SIGN("11","区块双签"),
        ;
        private static Map<String, TypeEnum> map = new HashMap<>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
        }
        public static TypeEnum getEnum(String code){
            return map.get(code);
        }
        private String code;
        private String desc;
        TypeEnum ( String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }
    /**
     * 交易结果成败枚举类：
     *  1.成功
     *  2.失败
     */
    public enum StatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(0, "失败")
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }
}