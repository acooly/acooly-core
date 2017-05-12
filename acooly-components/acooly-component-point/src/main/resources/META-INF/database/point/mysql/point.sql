/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50535
Source Host           : 127.0.0.1:9527
Source Database       : pl_acooly_point

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2017-03-13 17:37:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `point_account`
-- ----------------------------
DROP TABLE IF EXISTS `point_account`;
CREATE TABLE `point_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '积分余额',
  `freeze` bigint(20) NOT NULL DEFAULT '0' COMMENT '冻结',
  `total_expense_point` bigint(20) NOT NULL DEFAULT '0' COMMENT '总消费积分',
  `total_produce_point` bigint(20) NOT NULL DEFAULT '0' COMMENT '总产生积分',
  `statistics_balance` bigint(20) DEFAULT NULL COMMENT '统计积分',
  `status` varchar(50) NOT NULL COMMENT '状态 {invalid:无效,valid:有效}',
  `grade_id` int(11) NOT NULL COMMENT '用户等级',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分账户';

ALTER TABLE `point_account`
  ADD UNIQUE INDEX `acc_username_idx` (`user_name` ASC)  COMMENT '';
-- ----------------------------
-- Records of point_account
-- ----------------------------

-- ----------------------------
-- Table structure for `point_grade`
-- ----------------------------
DROP TABLE IF EXISTS `point_grade`;
CREATE TABLE `point_grade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `num` int(11) NOT NULL DEFAULT '1' COMMENT '等级',
  `title` varchar(63) NOT NULL COMMENT '标题',
  `start_point` bigint(11) NOT NULL DEFAULT '0' COMMENT '积分区间_开始',
  `end_point` bigint(11) NOT NULL DEFAULT '0' COMMENT '积分区间_结束',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `picture` varchar(255) DEFAULT NULL COMMENT ' 图标',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='积分等级';

-- ----------------------------
-- Records of point_grade
-- ----------------------------
INSERT INTO `point_grade` VALUES ('1', '1', '等级1', '0', '10000000', '2015-12-28 18:46:34', '2016-01-11 18:40:53', '/null/2015/12/29/201512290011168415068.jpg', '');
INSERT INTO `point_grade` VALUES ('2', '2', '等级2', '10000001', '30000000', '2015-12-28 18:47:09', '2016-01-11 18:39:03', '/null/2015/12/29/201512290012023018029.jpg', '');
INSERT INTO `point_grade` VALUES ('3', '3', '等级3', '30000001', '50000000', '2015-12-28 18:47:42', '2016-01-11 18:38:57', '/point/2015/12/31/201512312313018322072.png', '');
INSERT INTO `point_grade` VALUES ('4', '4', '等级4', '50000001', '999999999', '2015-12-28 19:38:21', '2016-12-25 15:00:13', '/point/2015/12/31/201512312314404960686.png', '');
INSERT INTO `point_grade` VALUES ('5', '5', '等级5', '100000000', '999999999', '2015-12-30 15:42:27', '2017-02-04 01:17:51', '/point/2015/12/31/201512312321126015457.png', '');

-- ----------------------------
-- Table structure for `point_statistics`
-- ----------------------------
DROP TABLE IF EXISTS `point_statistics`;
CREATE TABLE `point_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(与customer共主键)',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `num` bigint(20) DEFAULT NULL COMMENT '统计条数',
  `point` bigint(20) NOT NULL DEFAULT '0' COMMENT '积分余额',
  `actual_point` bigint(20) DEFAULT NULL COMMENT '真实清零积分',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` varchar(32) NOT NULL COMMENT '状态 {init:初始化,finish:完成}',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分统计';

-- ----------------------------
-- Records of point_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for `point_trade`
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
  `busi_id` varchar(32) DEFAULT NULL COMMENT '相关业务id',
  `busi_type` varchar(40) DEFAULT NULL COMMENT '相关业务类型',
  `busi_type_text` varchar(64) DEFAULT NULL COMMENT '相关业务类型描述',
  `busi_data` varchar(256) DEFAULT NULL COMMENT '相关业务数据',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='积分交易信息';



-- ----------------------------
-- Records of point_trade
-- ----------------------------

DROP TABLE IF EXISTS `point_clear_config`;
CREATE TABLE `point_clear_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `start_trade_time` datetime NOT NULL COMMENT '开始交易时间',
  `end_trade_time` datetime NOT NULL COMMENT '结束交易时间',
  `start_clear_time` datetime NOT NULL COMMENT '开始清理时间',
  `end_clear_time` datetime NOT NULL COMMENT '结束清理时间',
  `clear_time` datetime NOT NULL COMMENT '清零时间',
  `status` varchar(32) NOT NULL COMMENT '状态 {init:未完成,finish:完成}',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `memo` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='积分清零设置';


