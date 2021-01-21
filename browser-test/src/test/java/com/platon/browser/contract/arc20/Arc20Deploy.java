package com.platon.browser.contract.arc20;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 部署Alat合约
 */
@Slf4j
public class Arc20Deploy extends Arc20Base {
    private Integer deployCount = 10;
    // 部署合约
    @Test
    public void deploy() throws Exception {
        for(int i =0;i<=deployCount;i++){
            log.error("adminWallet balance:{}",getBalance(adminWallet.getAddress()));
            String str = randomStr();
            Arc20Contract contract = Arc20Contract.deploy(
                    WEB3J,
                    adminWallet,
                    gasProvider,
                    CHAIN_ID,
                    new BigInteger("10000000000000000000"),
                    "symbol-"+str,
                    BigInteger.valueOf(16L),
                    "name-"+str,
                    adminWallet.getAddress()
            ).send();
            try{
                FileUtils.write(new File(contractAddressFilePath),contract.getContractAddress()+"\n", "UTF-8",true);
            }catch (IOException e){
                log.error("",e);
            }
            log.info(JSON.toJSONString(contract,true));
        }
    }
}