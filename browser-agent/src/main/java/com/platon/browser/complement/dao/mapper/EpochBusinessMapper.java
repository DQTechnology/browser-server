package com.platon.browser.complement.dao.mapper;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.entity.Staking;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Auther: dongqile
 * @Date:  2019/11/5
 * @Description:
 */
public interface EpochBusinessMapper {
    /**
     * 新结算周期
     */
    @Transactional
    void settle ( BusinessParam param);
    /**
     * 更新Gas估算周期数
     */
    @Transactional
    void updateGasEstimate (@Param("list") List<GasEstimate> estimateList);

    /**
     * 新选举周期数据变更（结算&共识周期往前推20个块）
     */
    void slashNode (BusinessParam param);


    /**
     * 新共识周期数据变更
     */
    @Transactional
    void consensus(BusinessParam param);
    
    /**
     * 查询待惩罚的节点列表
     * @param preValidatorList
     * @return
     */
	List<Staking> querySlashNode(@Param("list") List<String> preValidatorList);

    /**
     * 委托奖励操作
     * @param exitNodeIds
     */
    void rotateDelegateReward(@Param("list") List<String> exitNodeIds);
}