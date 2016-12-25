/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-23 23:57 创建
 */
package com.acooly.core.common.boot.component.cache;

import com.acooly.core.common.boot.component.cache.declarative.DefaultCachingConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * @author qiubo
 */
@Configuration
@EnableConfigurationProperties({ CacheProperties.class })
public class RedisAutoConfig {
	
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setDefaultSerializer(springSessionDefaultRedisSerializer());
		return template;
	}
	
	@Bean
	public CachingConfigurer cachingConfigurer(RedisTemplate redisTemplate, CacheProperties cacheProperties) {
		return new DefaultCachingConfigurer(redisTemplate, cacheProperties.getExpireTime());
	}
	//session使用kryo序列化器 ref:org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration.setDefaultRedisSerializer()
	@Bean("springSessionDefaultRedisSerializer")
	public DefaultRedisSerializer springSessionDefaultRedisSerializer(){
		return new DefaultRedisSerializer();
	}
}
