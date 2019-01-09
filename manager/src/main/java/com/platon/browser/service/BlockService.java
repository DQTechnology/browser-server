package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.*;

import java.util.List;

public interface BlockService {
    RespPage<BlockListItem> getBlockPage(BlockPageReq req);
    BlockDetail getBlockDetail(BlockDetailReq req);

    BlockDetail getBlockDetailNavigate(BlockDetailNavigateReq req);
    List<Block> getBlockList(BlockDownloadReq req);
}
