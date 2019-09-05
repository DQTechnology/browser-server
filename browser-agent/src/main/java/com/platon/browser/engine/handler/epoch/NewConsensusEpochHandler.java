package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static java.lang.String.format;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 结算周期变更事件处理类
 */
@Component
public class NewConsensusEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;
    private StakingStage stakingStage;

    @Override
    public void handle(EventContext context) throws Exception {
        stakingStage = context.getStakingStage();
        updateValidator(); // 更新缓存中的辅助共识周期验证人信息
        updateStaking(); // 更新质押相关信息
    }

    /**
     * 更新与质押相关的信息
     */
    private void updateStaking() throws NoSuchBeanException {
        List<CustomStaking> stakingList = NODE_CACHE.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        // <节点ID, 前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)>
        Map<String,String> consensusInfo = new HashMap<>();
        String tpl = "前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)";
        for (CustomStaking staking:stakingList){
            Node nextNode = bc.getCurValidator().get(staking.getNodeId());
            // 看当前验证人是否在下一轮共识
            if(nextNode!=null){
                staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                // 累加共识周期期望区块数（提前设置下一轮期望的出块数）
                CustomNode customNode = NODE_CACHE.getNode(staking.getNodeId());
                customNode.setStatExpectBlockQty(customNode.getStatExpectBlockQty()+chainConfig.getExpectBlockCount().longValue());
            }else {
                staking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
            }

            String info = tpl.replace("PRE_QTY",staking.getPreConsBlockQty().toString())
                    .replace("CUR_QTY",staking.getCurConsBlockQty().toString())
                    .replace("VER_ROUND",staking.getStatVerifierTime().toString());
            consensusInfo.put(staking.getNodeId(),info);
            staking.setPreConsBlockQty(staking.getCurConsBlockQty());
            staking.setCurConsBlockQty(BigInteger.ZERO.longValue());
            stakingStage.updateStaking(staking);
        }

        // 下一轮验证人提前设置验证轮数：验证周期轮数+1
        for (Node node:bc.getCurValidator().values()){
            CustomNode customNode = NODE_CACHE.getNode(HexTool.prefix(node.getNodeId()));
            customNode.setStatVerifierTime(customNode.getStatVerifierTime()+1);
            CustomStaking latestStaking = customNode.getLatestStaking();
            customNode.setStatVerifierTime(customNode.getStatVerifierTime()+1);
            latestStaking.setStatVerifierTime(latestStaking.getStatVerifierTime()+1);
        }

        logger.debug("质押节点共识信息：{}", JSON.toJSONString(consensusInfo,true));
    }

    /**
     * 更新共识周期验证人和结算周期验证人映射缓存
     * // 假设当前链上最高区块号为750
     * 1         250        500        750
     * |----------|----------|----------|
     * A B C      A C D       B C D
     * 共识周期的临界块号分别是：1,250,500,750
     * 使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     * 如果当前区块号为753，由于未达到
     */
    private void updateValidator() throws Exception {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        BaseResponse<List <Node>> result;
        // ==================================更新前一周期验证人列表=======================================

        // 取入参区块号的前一共识周期结束块号，因此可以通过它查询前一共识周期验证人历史列表
        BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
        try {
            result = SpecialContractApi.getHistoryValidatorList(client.getWeb3j(),prevEpochLastBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,result.errMsg));
        }else{
            bc.getPreValidator().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
            logger.debug("前一轮共识周期(最后块号{})验证人(查{}):{}",blockNumber,blockNumber,JSON.toJSONString(bc.getPreValidator(),true));
        }


        // ==================================更新下一共识周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
        result = SpecialContractApi.getHistoryValidatorList(client.getWeb3j(),nextEpochFirstBlockNumber);
        if(result.isStatusOk()){
            bc.getCurValidator().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
            logger.debug("下一轮共识周期验证人(查{}):{}",nextEpochFirstBlockNumber,JSON.toJSONString(bc.getCurValidator(),true));
        }
        if (!result.isStatusOk()) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                result = client.getNodeContract().getValidatorList().send();
                bc.getCurValidator().clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
                logger.debug("下一轮共识周期验证人(实时):{}",JSON.toJSONString(bc.getCurValidator(),true));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CandidateException(format("【查询下一轮共识验证人-底层出错】查询实时共识周期验证人出错:%s",e.getMessage()));
            }
            if(!result.isStatusOk()){
                throw new CandidateException(format("【查询下一轮共识验证人-底层出错】查询实时共识周期验证人出错:%s",result.errMsg));
            }
        }

        if(bc.getCurValidator().size()==0){
            throw new CandidateException("查询不到下一轮共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+bc.getCurConsensusEpoch()+")");
        }
        logger.debug("下一轮共识周期验证人:{}",JSON.toJSONString(bc.getCurVerifier(),true));
    }
}
