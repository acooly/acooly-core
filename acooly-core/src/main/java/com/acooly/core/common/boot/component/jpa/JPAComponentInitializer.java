/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 21:21 创建
 */
package com.acooly.core.common.boot.component.jpa;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static com.acooly.core.common.boot.component.jpa.JPAProperties.ENABLE_KEY;


/**
 * @author qiubo
 */
public class JPAComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (!applicationContext.getEnvironment().getProperty(ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
			System.setProperty("spring.data.jpa.repositories.enabled", "false");
		}
		//因为shiro的原因，使用filter代替 ref:com.acooly.core.common.boot.component.jpa.JPAAutoConfig.openEntityManagerInViewFilter
		System.setProperty("spring.jpa.open-in-view", "false");
		//关闭jpa校验
		System.setProperty("spring.jpa.properties.javax.persistence.validation.mode", "none");
	}
}
