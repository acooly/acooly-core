# execute some ddl at dao inited

CREATE TABLE `acooly_coder_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `birthday` date NOT NULL COMMENT '生日',
  `gender` tinyint(4) NOT NULL COMMENT '性别 {1:男,2:女,3:人妖}',
  `real_name` varchar(16) NOT NULL COMMENT '姓名',
  `idcard_type` varchar(18) NOT NULL COMMENT '证件类型 {cert:身份证,pass:护照,other:其他}',
  `idcard_no` varchar(48) NOT NULL COMMENT '身份证号码',
  `mobile_no` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮件',
  `subject` varchar(64) DEFAULT NULL COMMENT '摘要',
  `customer_type` varchar(16) DEFAULT NULL COMMENT '客户类型 {normal:普通,vip:重要,sepc:特别}',
  `fee` decimal(12,2) DEFAULT NULL COMMENT '手续费',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 {0:无效,1:有效}',
  `content` text COMMENT '测试Text类型',
  `salary` int(11) DEFAULT NULL COMMENT '薪水(元)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='acoolycoder测试';

INSERT INTO `acooly_coder_customer` (`id`, `username`, `age`, `birthday`, `gender`, `real_name`, `idcard_type`, `idcard_no`, `mobile_no`, `mail`, `subject`, `customer_type`, `fee`, `status`, `content`, `salary`, `create_time`, `update_time`, `comments`)
VALUES
	(1, 'zhangpu', 37, '1982-12-15', 1, '张飞', 'cert', '51022119820915641X', '13896177630', '', '', 'normal', NULL, 1, 'sdfasdf', NULL, '2019-12-23 09:09:10', '2019-12-23 09:09:23', '');
