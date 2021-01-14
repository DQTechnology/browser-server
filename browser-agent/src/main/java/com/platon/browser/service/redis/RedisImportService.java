package com.platon.browser.service.redis;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.OldErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis数据批量入库服务
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class RedisImportService {
    @Resource
    private RedisBlockService redisBlockService;
    @Resource
    private RedisTransactionService redisTransactionService;
    @Resource
    private RedisStatisticService redisStatisticService;
    @Resource
    private OldRedisErc20TxService oldRedisErc20TxService;
    @Resource
    private RedisErc20TxService redisErc20TxService;
    @Resource
    private RedisErc721TxService redisErc721TxService;

    private static final int SERVICE_COUNT = 6;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(SERVICE_COUNT);

    private <T> void submit(AbstractRedisService<T> service, Set<T> data, boolean serialOverride, CountDownLatch latch){
        EXECUTOR.submit(()->{
            try {
                service.save(data,serialOverride);
            } catch (Exception e) {
                log.error("",e);
            }finally {
                latch.countDown();
            }
        });
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions, Set<NetworkStat> statistics) throws InterruptedException {
        log.debug("Redis批量导入:{}(blocks({}),transactions({}),statistics({})",Thread.currentThread().getStackTrace()[1].getMethodName(),blocks.size(),transactions.size(),statistics.size());
        long startTime = System.currentTimeMillis();
        try{
            Set<OldErcTx> oldErc20TxList = getOldErc20TxList(transactions);
            Set<ErcTx> erc20TxList = getErc20TxList(transactions);
            Set<ErcTx> erc721TxList = getErc721TxList(transactions);
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);
            submit(redisBlockService,blocks,false,latch);
            submit(redisTransactionService,transactions,false,latch);
            submit(redisStatisticService,statistics,true,latch);
            submit(oldRedisErc20TxService, oldErc20TxList,false,latch);
            submit(redisErc20TxService, erc20TxList,false,latch);
            submit(redisErc721TxService, erc721TxList,false,latch);
            latch.await();
            log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }

    /**
     * 取Token交易列表
     */
    public Set<OldErcTx> getOldErc20TxList(Set<Transaction> txSet){
        Set<OldErcTx> recordSet = new HashSet<>();
        if (txSet != null && !txSet.isEmpty()) {
            for (Transaction tx : txSet) {
                if (null != tx && null != tx.getOldErcTxes() && !tx.getOldErcTxes().isEmpty()) {
                    recordSet.addAll(tx.getOldErcTxes());
                }
            }
        }
        return recordSet;
    }

    /**
     * 取erc20交易列表
     */
    public Set<ErcTx> getErc20TxList(Set<Transaction> transactions){
        Set<ErcTx> result = new HashSet<>();
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction tx : transactions) {
                if (null != tx && null != tx.getOldErcTxes() && !tx.getOldErcTxes().isEmpty()) {
                    tx.getOldErcTxes().forEach(e->{
                        ErcTx ercTx = new ErcTx();
                        BeanUtils.copyProperties(e,ercTx);
                        result.add(ercTx);
                    });
                }
            }
        }
        return result;
    }

    /**
     * 取erc721交易列表
     */
    public Set<ErcTx> getErc721TxList(Set<Transaction> transactions){
        // TODO: 取出erc721交易
        Set<ErcTx> result = new HashSet<>();
//        if (transactions != null && !transactions.isEmpty()) {
//            for (Transaction tx : transactions) {
//                if (null != tx && null != tx.getEsTokenTransferRecords() && !tx.getEsTokenTransferRecords().isEmpty()) {
//                    tx.getEsTokenTransferRecords().forEach(e->{
//                        EsErcTx ercTx = new EsErcTx();
//                        BeanUtils.copyProperties(e,ercTx);
//                        result.add(ercTx);
//                    });
//                }
//            }
//        }
        return result;
    }
}