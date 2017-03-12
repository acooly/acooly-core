-- ----------------------------
--  Table structure for `lottery`
-- ----------------------------
CREATE TABLE `lottery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(32) NOT NULL COMMENT '抽奖编码',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `type` varchar(16) DEFAULT NULL COMMENT '类型 {roulette:轮盘抽奖,other:其他}',
  `max_winners` int(11) DEFAULT '0' COMMENT '最大中奖人数,0为无限制',
  `multi_play` int(11) DEFAULT NULL COMMENT '单人次数',
  `user_counter` varchar(16) NOT NULL COMMENT '是否参与计数',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(16) DEFAULT NULL COMMENT '状态{enable:正常,pause:暂停,disable:完结}',
  `note` varchar(512) DEFAULT NULL COMMENT '说明',
  `comments` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `uk_lottery_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `lottery`
-- ----------------------------
INSERT INTO `lottery` VALUES ('1', '20150316000000000001', '周年庆抽奖活动', 'roulette', '2015-03-25 00:00:00', '2015-04-15 23:59:59', '2015-03-25 00:00:00', null, 'pause', '周年庆抽奖活动', null, '0', '1', 'disable'), ('3', '20141213024403945589', '新用户注册', 'roulette', '2014-12-18 00:00:00', '2018-04-01 00:00:00', '2014-12-13 02:44:03', '2016-04-11 13:56:55', 'enable', 'asdfasdf', '', '0', '1', 'disable'), ('4', '150509003458102000', '首投活动', 'roulette', '2015-05-01 00:00:00', '2016-08-31 00:00:00', '2015-05-09 00:34:58', '2016-06-06 14:07:46', 'enable', '抽奖地址：\nhttp://www.1119e.com/portal/activities/novice/', '1.凡是在活动开始后（预计5月1日起）完成首次投资200元的客户，均获得1次（仅1次）抽奖机会；\n2.同一身份证号码的用户，只能抽奖1次；\n3.奖项设置：数量上限和概率见上图\n4.活动总中奖户数为2000人，达到后活动即结束', '2000', '1', 'disable'), ('5', '150720164644052000', '昆明站投资抽奖', 'roulette', '2015-07-13 00:00:00', '2015-08-31 00:00:00', '2015-07-20 16:46:45', null, 'pause', '阿萨德飞洒地方', '昆明站投资抽奖活动', '0', '1', 'disable'), ('6', '160112022252200000', '砸蛋抽奖-鸡蛋', 'other', '2016-01-01 00:00:00', '2016-03-01 00:00:00', '2016-01-12 02:27:58', '2016-02-29 09:28:37', 'enable', '', '砸蛋抽奖-鸡蛋', '0', '0', 'disable'), ('7', '160112022448200001', '砸蛋抽奖-银蛋', 'other', '2016-01-01 00:00:00', '2016-03-01 00:00:00', '2016-01-12 02:28:01', '2016-02-29 09:28:28', 'enable', '', '砸蛋抽奖-银蛋', '0', '0', 'disable'), ('8', '160112022633200002', '砸蛋抽奖-金蛋', 'other', '2016-01-01 00:00:00', '2016-03-01 00:00:00', '2016-01-12 02:28:03', '2016-02-29 09:28:15', 'enable', '', '砸蛋抽奖-金蛋', '0', '0', 'disable'), ('9', '160311000802102000', '2016年周年庆', 'roulette', '2016-03-18 00:00:00', '2016-04-11 00:00:00', '2016-03-11 00:08:03', '2016-04-10 14:04:04', 'enable', '自2015年1月1日起至2015年12月31日（含当日）止，累计投资金额超过3万元的用户，均可获赠一次抽奖机会\n2016年3月20日至2016年4月10日期间，单个借款标投资金额每满1万元，可获赠1次抽奖机会，上不封顶\n奖金将于5个工作日内发放至中奖用户在平台的账户中', '活动时间：2016年3月20日-2016年4月10日\n', '0', '1000', 'enable');


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
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `comments` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `lottery_count`
-- ----------------------------
BEGIN;
INSERT INTO `lottery_count` VALUES ('1', '634ulimit', '3172', '2016-01-19 17:27:22', '2016-01-19 17:27:22', null, '34', '6'), ('2', '633ulimit', '973', '2016-01-19 17:28:00', '2016-01-19 17:28:00', null, '33', '6'), ('3', '737ulimit', '330', '2016-01-19 17:31:40', '2016-01-19 17:31:40', null, '37', '7'), ('4', '635ulimit', '517', '2016-01-19 17:32:02', '2016-01-19 17:32:02', null, '35', '6'), ('5', '738ulimit', '300', '2016-01-19 17:34:26', '2016-01-19 17:34:26', null, '38', '7'), ('6', '736ulimit', '219', '2016-01-21 21:44:20', '2016-01-21 21:44:20', null, '36', '7'), ('7', '839ulimit', '74', '2016-01-21 21:54:17', '2016-01-21 21:54:17', null, '39', '8'), ('8', '840ulimit', '41', '2016-01-21 21:54:48', '2016-01-21 21:54:48', null, '40', '8'), ('9', '841ulimit', '2', '2016-02-01 15:34:31', '2016-02-01 15:34:31', null, '41', '8'), ('10', '947ulimit', '671', '2016-03-18 11:43:02', '2016-03-18 11:43:02', null, '47', '9'), ('11', '943ulimit', '673', '2016-03-18 11:52:18', '2016-03-18 11:52:18', null, '43', '9'), ('12', '949ulimit', '374', '2016-03-18 11:52:33', '2016-03-18 11:52:33', null, '49', '9'), ('13', '945ulimit', '227', '2016-03-18 11:57:04', '2016-03-18 11:57:04', null, '45', '9'), ('14', '946ulimit', '19', '2016-03-18 13:19:04', '2016-03-18 13:19:04', null, '46', '9'), ('15', '944ulimit', '2', '2016-03-19 23:31:26', '2016-03-19 23:31:26', null, '44', '9'), ('16', '942ulimit', '1', '2016-03-25 12:03:33', '2016-03-25 12:03:33', null, '42', '9');
COMMIT;

-- ----------------------------
--  Table structure for `lottery_user_count`
-- ----------------------------
CREATE TABLE `lottery_user_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `lottery_id` bigint(20) NOT NULL,
  `lottery_title` varchar(64) DEFAULT NULL COMMENT '活动标题',
  `user` varchar(64) NOT NULL COMMENT '参与人',
  `total_times` int(11) NOT NULL DEFAULT '0' COMMENT '获参次数',
  `play_times` int(11) NOT NULL COMMENT '参与次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `comments` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_LOTTERY_USER_COUNT` (`lottery_id`,`user`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=508 DEFAULT CHARSET=utf8;

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


