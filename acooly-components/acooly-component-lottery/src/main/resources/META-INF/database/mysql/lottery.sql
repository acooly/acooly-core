-- ----------------------------
--  Table structure for `lottery`
-- ----------------------------
CREATE TABLE `lottery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(32) NOT NULL COMMENT '抽奖编码',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `type` varchar(16) DEFAULT NULL COMMENT '类型 {roulette:轮盘抽奖,other:其他}',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(16) DEFAULT NULL COMMENT '状态{enable:正常,pause:暂停,disable:完结}',
  `comments` varchar(256) DEFAULT NULL COMMENT '备注',
  `note` varchar(512) DEFAULT NULL COMMENT '说明',
  `max_winners` int(11) DEFAULT '0' COMMENT '最大中奖人数,0为无限制',
  `multi_play` int(11) DEFAULT NULL COMMENT '单人次数',
  `user_counter` varchar(16) NOT NULL COMMENT '是否参与计数',
  PRIMARY KEY (`id`),
  KEY `uk_lottery_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `lottery`
-- ----------------------------
BEGIN;
INSERT INTO `lottery` VALUES ('1', '20150316000000000001', '抽奖活动案例', 'roulette', '2017-03-01 00:00:00', '2018-03-30 00:00:00', '2015-03-25 00:00:00', '2017-03-13 10:59:20', 'enable', '周年庆抽奖活动', '1213', '0', '1000', 'disable');
COMMIT;


-- ----------------------------
--  Table structure for `lottery_award`
-- ----------------------------
CREATE TABLE `lottery_award` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(32) DEFAULT NULL COMMENT '奖项编码',
  `lottery_id` bigint(20) NOT NULL COMMENT '抽奖id',
  `award_type` varchar(32) DEFAULT NULL COMMENT '奖励类型',
  `award_amount` bigint(20) DEFAULT NULL COMMENT '金额(分)',
  `award` varchar(32) NOT NULL COMMENT '奖项',
  `award_note` varchar(256) DEFAULT NULL COMMENT '奖项说明',
  `award_photo` varchar(128) DEFAULT NULL COMMENT '奖项图片',
  `award_position` varchar(32) DEFAULT NULL COMMENT '位置(如:度数)',
  `weight` int(11) NOT NULL COMMENT '权重',
  `max_period` varchar(16) DEFAULT NULL COMMENT '最大人数周期',
  `max_winer` int(11) NOT NULL COMMENT '最大中奖数',
  `record_winner` varchar(16) DEFAULT NULL COMMENT '保持中奖纪录',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `comments` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `lottery_award`
-- ----------------------------
BEGIN;
INSERT INTO `lottery_award` VALUES ('1', '17031216483500400000', '1', 'money', '111900', '1119元', '大奖1119元', null, '339,23', '1', 'day', '5', 'enable', '2014-12-14 01:47:02', '2017-03-12 22:20:56', ''), ('2', '17031301441200400000', '1', 'money', '10000', '100元', '现金100元', null, '23,68', '2', 'ulimit', '0', 'enable', '2014-12-14 01:47:40', '2017-03-13 01:44:12', ''), ('3', '17031301441900400000', '1', 'money', '5000', '50元', '现金50元', null, '69,113', '10', 'ulimit', '0', 'enable', '2014-12-14 01:48:19', '2017-03-13 01:44:20', ''), ('4', '17031217053900400000', '1', 'money', '500', '5元', '现金5元', null, '114,158', '50', 'day', '5', 'enable', '2014-12-14 01:48:43', '2017-03-13 01:44:27', ''), ('5', '17031301440200400000', '1', 'virtual', '0', '1年vip', '送1年vip服务', null, '294,338', '5', 'ulimit', '0', 'enable', '2014-12-14 01:49:20', '2017-03-13 01:44:02', ''), ('6', '17031301445100400000', '1', 'goods', '0', '虎牌电饭煲', '虎牌电饭煲', null, '249,293', '10', 'ulimit', '0', 'enable', '2014-12-14 01:49:50', '2017-03-13 01:44:51', ''), ('7', '17031301451400400000', '1', 'goods', '0', '小米手环', '小米手环', null, '204,248', '20', 'ulimit', '0', 'enable', '2014-12-14 01:50:15', '2017-03-13 01:45:15', ''), ('8', '17031300551700400000', '1', 'goods', '0', '谢谢参与', '未中奖的奖项，必须设置', null, '159,203', '50', 'ulimit', '0', 'disable', '2014-12-14 01:51:01', '2017-03-13 01:45:32', '');
COMMIT;



-- ----------------------------
--  Table structure for `lottery_count`
-- ----------------------------
CREATE TABLE `lottery_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ukey` varchar(64) NOT NULL COMMENT '关键字',
  `count` int(11) NOT NULL COMMENT '计数值',
  `award_id` bigint(20) NOT NULL COMMENT '奖项ID',
  `lottery_id` bigint(20) NOT NULL COMMENT '抽奖ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_L_COUNT_UKEY` (`ukey`),
  KEY `IDX_LCOUNT_LID` (`lottery_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ----------------------------
--  Table structure for `lottery_user_count`
-- ----------------------------
CREATE TABLE `lottery_user_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `lottery_id` bigint(20) NOT NULL,
  `lottery_code` bigint(20) NOT NULL,
  `lottery_title` varchar(64) DEFAULT NULL COMMENT '活动标题',
  `user` varchar(64) NOT NULL COMMENT '参与人',
  `total_times` int(11) NOT NULL DEFAULT '0' COMMENT '获参次数',
  `play_times` int(11) NOT NULL COMMENT '参与次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_LOTTERY_USER_COUNT` (`lottery_id`,`user`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `lottery_whitelist`
-- ----------------------------
CREATE TABLE `lottery_whitelist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `lottery_id` bigint(20) NOT NULL COMMENT '抽奖ID',
  `award_id` bigint(20) NOT NULL COMMENT '奖项ID',
  `user` varchar(64) NOT NULL COMMENT '抽奖用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` varchar(16) DEFAULT NULL COMMENT '状态 {enable:有效,disable:禁用,finish:完成}',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `lottery_winner`
-- ----------------------------
CREATE TABLE `lottery_winner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `lottery_id` bigint(20) NOT NULL COMMENT '抽奖id',
  `award_id` bigint(20) NOT NULL COMMENT '奖项id',
  `lottery_title` varchar(64) NOT NULL COMMENT '抽奖标题',
  `award_type` varchar(32) DEFAULT NULL COMMENT '奖项类型',
  `award_amount` bigint(20) DEFAULT NULL COMMENT '金额(分)',
  `order_no` varchar(64) DEFAULT NULL COMMENT '放款订单号',
  `award` varchar(32) NOT NULL COMMENT '奖项',
  `winner` varchar(32) NOT NULL COMMENT '中奖人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `status` varchar(16) NOT NULL COMMENT '状态 {winning:中奖,award:已发奖}',
  `comments` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10482 DEFAULT CHARSET=utf8;


