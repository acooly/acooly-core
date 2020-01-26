CREATE TABLE `ofile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `input_name` varchar(64) DEFAULT NULL COMMENT '表单名称',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `file_ext` varchar(8) DEFAULT NULL COMMENT '扩展名',
  `file_name` varchar(64) NOT NULL COMMENT '文件名',
  `file_path` varchar(128) NOT NULL COMMENT '图片路径',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `file_type` varchar(16) NOT NULL COMMENT '文件类型',
  `metadatas` varchar(255) DEFAULT NULL COMMENT '元数据，可以是JSON格式',
  `module` varchar(16) DEFAULT NULL COMMENT '模块分类',
  `object_id` varchar(64) NOT NULL COMMENT '对象ID',
  `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `status` varchar(16) DEFAULT 'enable' COMMENT '状态',
  `thumbnail` varchar(128) DEFAULT NULL COMMENT '缩略图路径',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;