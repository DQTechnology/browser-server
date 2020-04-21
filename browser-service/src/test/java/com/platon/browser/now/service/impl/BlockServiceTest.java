package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockServiceTest {

	@Mock
	private StatisticCacheService statisticCacheService;

	@Mock
	private BlockESRepository blockESRepository;

	@Mock
	private I18nUtil i18n;
	
	@Mock
	private CommonService commonService;

	@Spy
    private BlockServiceImpl target;

	@Before
	public void setup() throws IOException {
		ReflectionTestUtils.setField(target,"i18n",i18n);
		ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
		ReflectionTestUtils.setField(target,"commonService",commonService);
		ReflectionTestUtils.setField(target,"blockESRepository",blockESRepository);
		when(commonService.getNodeName(any(),any())).thenReturn("test-name");
		NetworkStat net = new NetworkStat();
		net.setCurNumber(10l);
		when(statisticCacheService.getNetworkStatCache()).thenReturn(net);
		Block block = new Block();
		block.setReward("10");
		block.setTime(new Date());
		block.setNum(10l);
		when(blockESRepository.get(any(),any())).thenReturn(block);
	}

	@Test
	public void blockDetails() throws IOException {
		
		
		BlockDetailsReq req = new BlockDetailsReq();
		req.setNumber(10);
		BlockDetailResp blockDetailResp = target.blockDetails(req);
		
		req.setNumber(0);
		blockDetailResp = target.blockDetails(req);
		assertNotNull(blockDetailResp);
	}
	
	@Test
	public void blockList() throws IOException {
		List<Block> blocks = new ArrayList<>();
		Block block = new Block();
		block.setNum(10l);
		block.setReward("10");
		block.setTime(new Date());
		blocks.add(block);
		
		Block block1 = new Block();
		block1.setNum(110l);
		block1.setReward("10");
		block1.setTime(new Date());
		blocks.add(block1);
		when(statisticCacheService.getBlockCache(any(),any())).thenReturn(blocks);
		
		PageReq pageReq = new PageReq();
        RespPage<BlockListResp> pages = target.blockList(pageReq);
        
        pageReq.setPageNo(10);
        target.blockList(pageReq);
        
        pageReq.setPageNo(100000);
        ESResult<Object> blockEs = new ESResult<>();
        List<Object> blockList = new ArrayList<>();
        blockList.add(block);
        blockList.add(block1);
        blockEs.setRsData(blockList);
        blockEs.setTotal(2l);
        when(blockESRepository.search(any(), any(), anyInt(),anyInt())).thenReturn(blockEs);
        target.blockList(pageReq);
        assertTrue(pages.getData().size()>=0);
	}
	
	@Test
	public void blockDetailNavigate() {
		BlockDetailNavigateReq req = new BlockDetailNavigateReq();
		req.setNumber(0l);
		req.setDirection("next");
		BlockDetailResp blockDetailResp = target.blockDetailNavigate(req);
		
		req.setNumber(0l);
		req.setDirection("prev");
		target.blockDetailNavigate(req);
		assertNotNull(blockDetailResp);
	}
}