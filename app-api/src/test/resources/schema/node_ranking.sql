CREATE TABLE `node_ranking` (
        `id` bigint(64) NOT NULL AUTO_INCREMENT,
        `node_id` varchar(255) NOT NULL COMMENT '节点ID',
        `ip` varchar(32) NOT NULL COMMENT '节点IP',
        `port` int(10) NOT NULL COMMENT '节点端口',
        `name` varchar(32) DEFAULT NULL COMMENT '节点名称',
        `type` int(4) NOT NULL COMMENT '节点类型',
        `intro` varchar(255) DEFAULT NULL COMMENT '节点简介',
        `address` varchar(64) NOT NULL COMMENT '节点地址',
        `deposit` varchar(255) NOT NULL COMMENT '质押金，单位-ADP',
        `org_name` varchar(64) DEFAULT NULL COMMENT '机构名称',
        `org_website` varchar(255) DEFAULT NULL COMMENT '机构官网',
        `chain_id` varchar(64) NOT NULL COMMENT '链ID',
        `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        `reward_ratio` double DEFAULT NULL COMMENT '分红比例:小数',
        `ranking` int(4) NOT NULL COMMENT '节点排名',
        `election_status` int(4) NOT NULL COMMENT '竞选状态\r\n1-候选前100名\r\n2-出块中\r\n3-验证节点\r\n4-备选前100名',
        `is_valid` int(4) NOT NULL COMMENT '是否有效节点 \r\n 1 ：有效 \r\n 0 ：无效',
        `url` varchar(255) NOT NULL COMMENT 'logo上传path',
        `begin_number` bigint(20) NOT NULL COMMENT '开始区块',
        `end_number` bigint(20) DEFAULT NULL COMMENT '结束区块',
        `block_count` bigint(20) NOT NULL COMMENT '出块数',
        `reward_amount` varchar(255) DEFAULT NULL COMMENT '累计分红',
        `profit_amount` varchar(255) DEFAULT NULL COMMENT '累计收益',
        `block_reward` varchar(255) DEFAULT NULL COMMENT '区块累计奖励',
        `country_code` varchar(64) DEFAULT NULL COMMENT '国家代码',
        `location` varchar(64) DEFAULT NULL COMMENT '地理位置',
        `longitude` varchar(64) DEFAULT NULL COMMENT '经度',
        `latitude` varchar(64) DEFAULT NULL COMMENT '纬度',
        `avg_time` double DEFAULT NULL COMMENT '平均出块时间,单位秒',
        `node_type` varchar(32) NOT NULL COMMENT '节点类型:\r\ncandidates—候选节点（备选）\r\nnominees—提名节点（候选）\r\nvalidator-验证节点',
        `count` bigint(20) DEFAULT NULL COMMENT '节点有效票',
                                PRIMARY KEY (`id`)
) ;
