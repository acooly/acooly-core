/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-23 23:57 创建
 */
package com.acooly.module.cache;

import com.acooly.module.cache.declarative.DefaultCachingConfigurer;
import com.acooly.module.cache.declarative.RedisInfoChecker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * @author qiubo
 */
@Configuration
@EnableConfigurationProperties({CacheProperties.class})
@EnableCaching
public class CacheAutoConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new DefaultKeySerializer());
        template.setDefaultSerializer(springSessionDefaultRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnProperty(value = "acooly.cache.checkVersion")
    public RedisInfoChecker checkRedisVersion(RedisTemplate redisTemplate) {
        RedisInfoChecker checker = new RedisInfoChecker(redisTemplate);
        checker.checkRedisVersion();
        return checker;
    }

    @Bean
    public CachingConfigurer cachingConfigurer(
            RedisTemplate redisTemplate, CacheProperties cacheProperties) {
        return new DefaultCachingConfigurer(redisTemplate, cacheProperties.getExpireTime());
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(CachingConfigurer cachingConfigurer) {
        return cachingConfigurer.cacheManager();
    }

    //session使用kryo序列化器 ref:org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration.setDefaultRedisSerializer()
    //	@Bean("springSessionDefaultRedisSerializer")
    //由于实体对象中有懒加载集合，不能用kryo。。。
    public DefaultRedisSerializer springSessionDefaultRedisSerializer() {
        return new DefaultRedisSerializer();
    }
}
