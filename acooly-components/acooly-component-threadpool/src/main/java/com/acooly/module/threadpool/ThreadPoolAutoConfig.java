/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-29 14:39 创建
 */
package com.acooly.module.threadpool;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ ThreadPoolProperties.class })
public class ThreadPoolAutoConfig {
	@Bean
	public ThreadPoolTaskExecutor commonTaskExecutor(ThreadPoolProperties properties) {
		ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
		bean.setCorePoolSize(properties.getThreadMin());
		bean.setMaxPoolSize(properties.getThreadMax());
		bean.setQueueCapacity(properties.getThreadQueue());
		bean.setKeepAliveSeconds(300);
		bean.setWaitForTasksToCompleteOnShutdown(true);
		bean.setAllowCoreThreadTimeOut(true);
		bean.setThreadNamePrefix("common-thread-pool-");
		bean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return bean;
	}
}
