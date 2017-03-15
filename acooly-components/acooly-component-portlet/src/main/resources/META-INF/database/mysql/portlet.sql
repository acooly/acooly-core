CREATE TABLE `p_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(16) NOT NULL COMMENT '类型 {suggest:建议,complaint:投诉}',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `content` varchar(512) NOT NULL COMMENT '内容',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `telephone` varchar(21) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(128) DEFAULT NULL COMMENT '联系地址',
  `contact_info` varchar(255) DEFAULT NULL COMMENT '联系信息',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='客户反馈';


CREATE TABLE `p_portlet_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` VARCHAR(45) NULL COMMENT '类型 {def:默认}',
  `title` VARCHAR(45) NOT NULL COMMENT '标题',
  `key` VARCHAR(45) NOT NULL COMMENT '参数键',
  `value` VARCHAR(4000) NULL COMMENT '参数值',
  `comments` VARCHAR(255) NULL COMMENT '备注',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`, `title`, `key`));
