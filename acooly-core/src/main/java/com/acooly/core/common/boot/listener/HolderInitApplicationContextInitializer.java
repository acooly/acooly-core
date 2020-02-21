/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:06 创建
 */
package com.acooly.core.common.boot.listener;

import com.acooly.core.common.boot.ApplicationContextHolder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author qiubo
 */
public class HolderInitApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, PriorityOrdered {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (applicationContext.getEnvironment().getPropertySources().get("bootstrap") == null) {
            new ApplicationContextHolder().setApplicationContext(applicationContext);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
