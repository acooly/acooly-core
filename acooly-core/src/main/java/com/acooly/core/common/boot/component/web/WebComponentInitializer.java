/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-04-12 23:33 创建
 */
package com.acooly.core.common.boot.component.web;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class WebComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (!applicationContext.getEnvironment().containsProperty("multipart.maxFileSize")) {
			System.setProperty("multipart.maxFileSize", "100Mb");
		}
		
		if (!applicationContext.getEnvironment().containsProperty("multipart.maxRequestSize")) {
			System.setProperty("multipart.maxRequestSize", "300Mb");
		}
	}
}
