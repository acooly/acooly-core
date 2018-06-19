/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by shuijing
 * date:2018-06-19
 */
package com.acooly.module.config.entity;


import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.domain.AbstractEntity;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.redis.core.RedisTemplate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.concurrent.TimeUnit;

/**
 * sys_app_config Entity
 *
 * @author qiubo
 * Date: 2018-06-19 21:52:29
 */
@Entity
@Table(name = "sys_app_config")
@Getter
@Setter
public class AppConfig extends AbstractEntity {
    public static final String CACHE_PREFIX = Apps.getAppName() + ".appConfig.";

    /**
     * 配置项名称
     */
    @NotEmpty
    @Size(max = 255)
    private String configName;

    /**
     * 配置值
     */
    @NotEmpty
    @Size(max = 255)
    private String configValue;

    /**
     * 配置描述
     */
    @NotEmpty
    @Size(max = 255)
    private String comments;

    /**
     * 本地缓存过期时间
     */
    private Integer localCacheExpire = 0;

    /**
     * redis缓存过期时间
     */
    private Integer redisCacheExpire = 600000;


    public void initLocalCache(Cache<String, AppConfig> cache) {
        if (localCacheExpire != 0) {
            cache.put(key(), this);
        }
    }

    public void initRedisCache(RedisTemplate<String, AppConfig> redisTemplate) {
        if (redisCacheExpire != 0) {
            redisTemplate.opsForValue().set(key(), this, this.getRedisCacheExpire(), TimeUnit.MILLISECONDS);
        }
    }


    public String key() {
        return key(this.getConfigName());
    }

    public static String key(String name) {
        return CACHE_PREFIX +  name;
    }
}