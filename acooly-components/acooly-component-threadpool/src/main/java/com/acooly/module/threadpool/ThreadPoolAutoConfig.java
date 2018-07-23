/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-29 14:39 创建
 */
package com.acooly.module.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ThreadPoolProperties.class})
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
        bean.setTaskDecorator(new LogTaskDecorator());
        bean.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return bean;
    }

    @Slf4j
    public static class LogTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            Runnable newR =
                    () -> {
                        try {
                            runnable.run();
                        } catch (Exception e) {
                            log.error("线程池任务处理异常", e);
                        }
                    };
            return newR;
        }
    }
}
