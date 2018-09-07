/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 21:21 创建
 */
package com.acooly.module.jpa;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.module.ds.DruidProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo
 */
public class JPAComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!applicationContext
                .getEnvironment()
                .getProperty(JPAProperties.ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
            System.setProperty("spring.data.jpa.repositories.enabled", "false");
        }
        DruidProperties druidProperties = Apps.buildProperties(DruidProperties.class);
        if (druidProperties.isAutoCreateTable()) {
            setPropertyIfMissing("spring.jpa.hibernate.ddl-auto", "update");
        } else {
            setPropertyIfMissing("spring.jpa.hibernate.ddl-auto", "none");
        }
        //因为shiro的原因，使用filter代替 ref:com.acooly.core.common.boot.component.jpa.JPAAutoConfig.openEntityManagerInViewFilter
        System.setProperty("spring.jpa.open-in-view", "false");
        //关闭jpa校验
        System.setProperty("spring.jpa.properties.javax.persistence.validation.mode", "none");
        setPropertyIfMissing("spring.data.redis.repositories.enabled","false");
    }
}
