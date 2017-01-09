/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-11-05 07:05 创建
 */
package com.acooly.module.cache.declarative;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 配置spring 声明式cache
 * <ul>
 * <li>1. 根据codis特性优化性能</li>
 * <li>2. 响应结果为null时不缓存数据，需要缓存null时，建议用空对象代替(我们的使用场景很少会对结果为null时缓存)</li>
 * <li>3. 当缓存操作异常时不抛出异常</li>
 * <li>4. 缓存key格式为:namespace+{@link Cacheable#cacheNames}+":"+param
 * </ul>
 *
 * @author qiubo
 */
public class DefaultCachingConfigurer implements CachingConfigurer {
	private RedisTemplate redisTemplate;
	private long expiration;
	
	public DefaultCachingConfigurer(RedisTemplate redisTemplate, long expiration) {
		this.redisTemplate = redisTemplate;
		this.expiration = expiration;
	}
	
	@Override
	public CacheManager cacheManager() {
		return new DefaultCacheManager(redisTemplate, expiration);
	}
	
	@Override
	public CacheResolver cacheResolver() {
		return null;
	}
	
	@Override
	public KeyGenerator keyGenerator() {
		return null;
	}
	
	@Override
	public CacheErrorHandler errorHandler() {
		return new DefaultCacheErrorHandler();
	}
}