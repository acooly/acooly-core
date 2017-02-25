/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.module.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static com.acooly.module.mail.MailProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ MailProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.mail")
public class MailAutoConfig {
	@Autowired
	private MailProperties mailProperties;
    @Bean
    public ThreadPoolTaskExecutor mailTaskExecutor() {
        ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
        bean.setCorePoolSize(mailProperties.getThreadMin());
        bean.setMaxPoolSize(mailProperties.getThreadMax());
        bean.setQueueCapacity(mailProperties.getThreadQueue());
        bean.setKeepAliveSeconds(300);
        bean.setWaitForTasksToCompleteOnShutdown(true);
        bean.setAllowCoreThreadTimeOut(true);
        bean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return bean;
    }
}
