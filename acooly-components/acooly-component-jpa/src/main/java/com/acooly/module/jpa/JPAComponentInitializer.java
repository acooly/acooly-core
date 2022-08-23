/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 21:21 创建
 */
package com.acooly.module.jpa;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo
 */
public class JPAComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!applicationContext.getEnvironment().getProperty(JPAProperties.ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
            System.setProperty("spring.data.jpa.repositories.enabled", "false");
        }
        setPropertyIfMissing("spring.jpa.hibernate.ddl-auto", "none");
        setPropertyIfMissing("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "false");
        setPropertyIfMissing("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        //因为shiro的原因，使用filter代替 ref:com.acooly.core.common.boot.component.jpa.JPAAutoConfig.openEntityManagerInViewFilter
        System.setProperty("spring.jpa.open-in-view", "false");
        //关闭jpa校验
        System.setProperty("spring.jpa.properties.javax.persistence.validation.mode", "none");
        setPropertyIfMissing("spring.data.redis.repositories.enabled", "false");
        // 开启实体和列的自动驼峰转换
        setPropertyIfMissing("spring.jpa.hibernate.naming.physical-strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        setPropertyIfMissing("spring.jpa.hibernate.naming.implicit-strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
    }
}
