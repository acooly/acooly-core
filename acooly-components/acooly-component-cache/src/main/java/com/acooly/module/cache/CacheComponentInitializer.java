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
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.core.utils.Ports;
import com.acooly.core.utils.ShutdownHooks;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ConfigurableApplicationContext;
import redis.embedded.RedisServer;

import java.io.IOException;

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
		if (Apps.isDevMode()) {
			RedisProperties redisProperties = new RedisProperties();
			EnvironmentHolder.buildProperties(redisProperties);
			String host = redisProperties.getHost();
			int port = redisProperties.getPort();
			if (host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) {
				if (!Ports.isPortUsing(port)) {
					if(SystemUtils.IS_OS_WINDOWS){
						log.info("发现redis服务没有启动，但是您使用的windows，请手动启动redis");
						return;
					}
					log.info("发现redis服务没有启动，使用内置redis用于开发测试");
					try {
						RedisServer redisServer = new RedisServer(6379);
						redisServer.start();
						ShutdownHooks.addShutdownHook(redisServer::stop, "内置redis关闭");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
}
