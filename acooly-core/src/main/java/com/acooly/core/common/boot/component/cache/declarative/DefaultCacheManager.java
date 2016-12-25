/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-11-05 07:03 创建
 */
package com.acooly.core.common.boot.component.cache.declarative;

import com.google.common.collect.Lists;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * @author qiubo
 */
public class DefaultCacheManager extends AbstractCacheManager {
    private RedisTemplate redisTemplate;
    private long expiration;

    public DefaultCacheManager(RedisTemplate redisTemplate, long expiration) {
        this.redisTemplate = redisTemplate;
        this.expiration = expiration;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Lists.newArrayList();
    }

    @Override
    protected Cache getMissingCache(String name) {
        return new DefaultCache(redisTemplate, name, expiration);
    }
}