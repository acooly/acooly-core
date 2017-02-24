/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-24 21:58 创建
 */
package com.acooly.module.sms;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static com.acooly.module.sms.SmsProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ SmsProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.sms")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class SmsAutoConfig {
	@Bean
	public AbstractDatabaseScriptIniter smsScriptIniter() {
		return new AbstractDatabaseScriptIniter() {
			@Override
			public String getEvaluateSql(DatabaseType databaseType) {
				return "SELECT count(*) FROM sys_sms_log";
			}
			
			@Override
			public List<String> getInitSqlFile(DatabaseType databaseType) {
				return Lists.newArrayList("META-INF/database/mysql/sms.sql");
			}
		};
	}
	
	@Bean
	public ThreadPoolTaskExecutor smsTaskExecutor(SmsProperties properties) {
		ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
		bean.setCorePoolSize(properties.getThreadMin());
		bean.setMaxPoolSize(properties.getThreadMax());
		bean.setQueueCapacity(properties.getThreadQueue());
		bean.setKeepAliveSeconds(300);
		bean.setWaitForTasksToCompleteOnShutdown(true);
		bean.setAllowCoreThreadTimeOut(true);
		bean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return bean;
	}
}
