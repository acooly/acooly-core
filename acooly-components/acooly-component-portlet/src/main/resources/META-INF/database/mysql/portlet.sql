CREATE TABLE `p_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(16) NOT NULL COMMENT '类型 {suggest:建议,complaint:投诉}',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `content` varchar(512) NOT NULL COMMENT '内容',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `telephone` varchar(21) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(128) DEFAULT NULL COMMENT '联系地址',
  `contact_info` varchar(255) DEFAULT NULL COMMENT '联系信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='客户反馈';
