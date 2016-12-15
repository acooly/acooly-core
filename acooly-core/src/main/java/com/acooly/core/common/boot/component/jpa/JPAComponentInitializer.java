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

import static com.acooly.core.common.boot.component.jpa.JpaConfig.JPADruidProperties.ENABLE_KEY;

/**
 * @author qiubo@yiji.com
 */
public class JPAComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (!applicationContext.getEnvironment().getProperty(ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
			System.setProperty("spring.data.jpa.repositories.enabled","false");
		}
	}
}
