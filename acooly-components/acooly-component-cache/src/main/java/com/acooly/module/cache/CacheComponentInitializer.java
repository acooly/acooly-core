/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-24 00:14 创建
 */
package com.acooly.module.cache;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.core.utils.ShutdownHooks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import redis.embedded.RedisServer;

/**
 * @author qiubo
 */
@Slf4j
public class CacheComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setPropertyIfMissing("spring.redis.pool.maxActive", "100");
        setPropertyIfMissing("spring.redis.pool.maxWait", "5000");
        setPropertyIfMissing("spring.session.redis.namespace", "session:" + Apps.getAppName());
        if (CacheProperties.isLocalRedisCanEnable()) {
            log.info("发现redis服务没有启动，使用内置redis用于开发测试");
            Thread thread = new Thread(
                    () -> {
                        try {
                            log.info("内置redis启动成功");
                            RedisServer redisServer = RedisServer.builder()
                                    .port(6379).setting("maxheap 128M").build();
                            redisServer.start();
                            ShutdownHooks.addShutdownHook(redisServer::stop, "内置redis关闭");
                        } catch (Exception e) {
                            log.warn("启动内置redis失败", e);
                        }
                    });
            thread.setName("redis-starter-thread");
            thread.start();
        }
    }
}
