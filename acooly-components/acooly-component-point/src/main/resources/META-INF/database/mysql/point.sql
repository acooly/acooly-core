
-- ----------------------------
-- Table structure for `pt_point_trade`
-- ----------------------------
DROP TABLE IF EXISTS `point_trade`;
CREATE TABLE `point_trade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `trade_no` varchar(32) NOT NULL COMMENT '交易订单号',
  `trade_type` varchar(64) NOT NULL COMMENT '交易类型{produce:产生,expense:消费}',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `account_id` bigint(20) NOT NULL COMMENT '积分账户ID',
  `amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易积分',
  `end_freeze` bigint(20) NOT NULL DEFAULT '0',
  `end_balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易后积分',
  `end_available` bigint(20) NOT NULL COMMENT '交易后有效积分',
  `business_data` varchar(256) DEFAULT NULL COMMENT '相关业务数据',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分交易信息';

-- ----------------------------
-- Records of pt_point_trade
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_point_grade`
-- ----------------------------
DROP TABLE IF EXISTS `point_grade`;
CREATE TABLE `point_grade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `num` int(11) NOT NULL DEFAULT '1' COMMENT '等级',
  `title` varchar(63) NOT NULL COMMENT '标题',
  `start_point` bigint(11) NOT NULL DEFAULT '0' COMMENT '积分区间_开始',
  `end_point` bigint(11) NOT NULL DEFAULT '0' COMMENT '积分区间_结束',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `picture` varchar(255) DEFAULT NULL COMMENT ' 图标',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='积分等级';

-- ----------------------------
-- Records of pt_point_grade
-- ----------------------------
INSERT INTO `point_grade` VALUES ('1', '1', '等级1', '0', '10000000', '2015-12-28 18:46:34', '2016-01-11 18:40:53', '/null/2015/12/29/201512290011168415068.jpg', '');
INSERT INTO `point_grade` VALUES ('2', '2', '等级2', '10000001', '30000000', '2015-12-28 18:47:09', '2016-01-11 18:39:03', '/null/2015/12/29/201512290012023018029.jpg', '');
INSERT INTO `point_grade` VALUES ('3', '3', '等级3', '30000001', '50000000', '2015-12-28 18:47:42', '2016-01-11 18:38:57', '/point/2015/12/31/201512312313018322072.png', '');
INSERT INTO `point_grade` VALUES ('4', '4', '等级4', '50000001', '999999999', '2015-12-28 19:38:21', '2016-12-25 15:00:13', '/point/2015/12/31/201512312314404960686.png', '');
INSERT INTO `point_grade` VALUES ('5', '5', '等级5', '100000000', '999999999', '2015-12-30 15:42:27', '2017-02-04 01:17:51', '/point/2015/12/31/201512312321126015457.png', '');

-- ----------------------------
-- Table structure for `point_account`
-- ----------------------------
DROP TABLE IF EXISTS `point_account`;
CREATE TABLE `point_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(与customer共主键)',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '积分余额',
  `freeze` bigint(20) NOT NULL DEFAULT '0' COMMENT '冻结',
  `status` varchar(50) NOT NULL COMMENT '状态 {invalid:无效,valid:有效}',
  `grade_id` int(11) NOT NULL COMMENT '用户等级',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分账户';

-- ----------------------------
-- Records of point_account
-- ----------------------------
