/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 *
 * 修订记录:
 * zhouxi@yiji.com 2015-09-15 15:41 创建
 *
 */
package com.acooly.module.filterchain;

import com.acooly.core.common.boot.EnvironmentHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiubo@yiji.com
 */

@Configuration
@ConditionalOnProperty(value = "acooly.filterchain.enable", matchIfMissing = true)
@EnableConfigurationProperties({ FilterchainProperties.class })
public class FilterchainAutoConfig {
	
	@Bean
	public static FilterBeanFactoryPostProcessor filterBeanFactoryPostProcessor(FilterchainProperties filterchainProperties) {
		EnvironmentHolder.buildProperties(filterchainProperties);
		return new FilterBeanFactoryPostProcessor(filterchainProperties.getScanPackage());
	}
	
}
