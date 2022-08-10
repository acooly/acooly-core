select now;

CREATE TABLE `city`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) DEFAULT NULL,
    `state`       varchar(255) DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` varchar(45)  DEFAULT NULL,
    `ext`         varchar(256) DEFAULT NULL,
    PRIMARY KEY (`id`)
) COMMENT ='市级信息';

CREATE TABLE `city1`
(
    `id`          varchar(255) COLLATE utf8_bin NOT NULL,
    `create_time` timestamp                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `name`        varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `state`       varchar(255) COLLATE utf8_bin          DEFAULT NULL,
    `update_time` timestamp                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
);

CREATE TABLE `app`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT,
    `display_name`    varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `name`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `parent_app_id`   bigint(20)                    DEFAULT NULL,
    `parent_id`       bigint(20)                    DEFAULT NULL,
    `raw_add_time`    datetime                      DEFAULT NULL,
    `raw_update_time` datetime                      DEFAULT NULL,
    `type`            varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `user_id`         bigint(20)                    DEFAULT NULL,
    `create_time`     datetime                      DEFAULT NULL,
    `update_time`     datetime                      DEFAULT NULL,
    `price`           int(11)                       DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `acooly_coder_customer`
(
    `id`               bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`         varchar(32) NOT NULL COMMENT '{title:’用户名’,type:’account’}',
    `age`              tinyint(4)           DEFAULT NULL COMMENT '年龄',
    `birthday`         date        NOT NULL COMMENT '生日',
    `gender`           varchar(16) NOT NULL COMMENT '{title:''性别’,alias: ‘gender’}',
    `animal`           varchar(16)          DEFAULT NULL COMMENT '{title:’生肖’, alias: ‘animal’}',
    `real_name`        varchar(16) NOT NULL COMMENT '{title:’姓名’,type:’chinese’}',
    `idcard_type`      varchar(18) NOT NULL COMMENT '{title:’证件类型’, type:’option’,options:{cert:’身份证‘,pass:’护照‘,other:’其他‘}}',
    `idcard_no`        varchar(48) NOT NULL COMMENT '{title:’身份证号码’,type:’idcard’,tip:’请使用真实的18位身份证号码’}',
    `bank_card_no`     varchar(48) NOT NULL COMMENT '{title:’银行卡卡号’,type:’bankcard’,tip:’请使用你常用的银行卡号，该卡号用于绑定验证身份和提现收益账户’}',
    `mobile_no`        varchar(11)          DEFAULT NULL COMMENT '{title:’手机号码’,type:’mobile’,tip:’请手机号码是自有使用，以确保后续所有业务通知您能收到。’}',
    `mail`             varchar(64)          DEFAULT NULL COMMENT '{title:’邮件’,type:’email’}',
    `customer_type`    varchar(16)          DEFAULT NULL COMMENT '{title:’客户类型’, type:’option’,options:{normal:’普通‘,vip:’重要‘,sepc:’特别‘}}',
    `subject`          varchar(128)         DEFAULT NULL COMMENT '摘要',
    `content`          text COMMENT '详情',
    `done_ratio`       int(11)              DEFAULT NULL COMMENT '{title:’完成度‘,type:’percent’,tip:’任务完成度，根据不同完成度获得对应的特权。<li>1、50%以下：基本会员权限</li><li>1、50%以上：VIP会员权限</li>’}',
    `pay_rate`         bigint(20)           DEFAULT NULL COMMENT '{title:’付款率‘,type:’centPercent’,tip:’演示说明：付款率字段采用支持2位小数的百分数（15.55%）。注意点如下: <li>1、数据库字段类型采用BIGINT</li><li>2、实体类型采用Money</li><li>3、数据库保存的是万分位值（例如:1555表示15.55%）</li>‘}',
    `salary`           int(11)              DEFAULT NULL COMMENT '{title:’薪水’,type:’money’}',
    `registry_channel` varchar(16)          DEFAULT NULL COMMENT '{title:’注册渠道’, alias: ‘channel’,tip:’alias属性演示：采用内置channel别名对应的枚举`ChannelEnum`生成下拉列表’}',
    `push_adv`         varchar(16)          DEFAULT NULL COMMENT '{title:’推送广告’, alias:’whether’}',
    `num_status`       tinyint(4)           DEFAULT NULL COMMENT '数字类型{1:A,2:B,3:C类型}',
    `website`          varchar(128)         DEFAULT NULL COMMENT '{title:’网址’,type:’url’}',
    `photo_path`       varchar(128)         DEFAULT NULL COMMENT '{title:’照片’,type:‘file’}',
    `status`           varchar(16) NOT NULL DEFAULT '1' COMMENT '{title:’状态’, alias:’simple’}',
    `create_time`      datetime    NOT NULL COMMENT '创建时间',
    `update_time`      datetime    NOT NULL COMMENT '更新时间',
    `comments`         varchar(255)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成客户信息';
