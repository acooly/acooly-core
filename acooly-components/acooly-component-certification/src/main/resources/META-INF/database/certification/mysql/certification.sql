CREATE TABLE `cert_certification_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(32) NOT NULL COMMENT '真实姓名',
  `id_car_no` varchar(18) NOT NULL COMMENT '身份证号',
  `sex`       varchar(18) DEFAULT NULL COMMENT '性别',
  `address`   varchar(512) DEFAULT NULL COMMENT '所在地址',
  `birthday`   varchar(32) DEFAULT NULL COMMENT '出生日期',
  `status`    int(2) DEFAULT 0 COMMENT '状态 {1:验证通过,0:未通过}',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='实名认证记录表';


