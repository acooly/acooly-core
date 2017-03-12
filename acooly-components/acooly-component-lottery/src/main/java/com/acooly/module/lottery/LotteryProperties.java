/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-10 16:11 创建
 */
package com.acooly.module.lottery;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.lottery.LotteryProperties.PREFIX;


/**
 * @author kuli@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class LotteryProperties implements InitializingBean {
    public static final String PREFIX = "acooly.lottery";

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
