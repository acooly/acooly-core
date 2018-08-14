/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-24 00:11 创建
 */
package com.acooly.module.cache;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.utils.Ports;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = "acooly.cache")
@Data
public class CacheProperties {

    private static final String IS_LOCAL_REDIS_ENABLE = "IS_LOCAL_REDIS_ENABLE";

    /**
     * 可选：基于注解的Spring CacheManager，设置缓存的过期时间。默认为0，即不会过期
     * 如果是使用RedisTemplate来显示读写缓存的，需要自己调用expire方法设置每个key的过期时间 单位秒
     */
    private int expireTime = 3600;

    public static boolean isLocalRedisCanEnable() {
        String property = System.getProperty(IS_LOCAL_REDIS_ENABLE);
        if (Strings.isNullOrEmpty(property)) {
            if (Apps.isDevMode()) {
                RedisProperties redisProperties = new RedisProperties();
                EnvironmentHolder.buildProperties(redisProperties);
                String host = redisProperties.getHost();
                int port = redisProperties.getPort();
                if (host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) {
                    if (!Ports.isPortUsing(port)) {
                        System.setProperty(IS_LOCAL_REDIS_ENABLE, Boolean.TRUE.toString());
                        return true;
                    }
                }
            }
            System.setProperty(IS_LOCAL_REDIS_ENABLE, Boolean.FALSE.toString());
            return false;
        }
        return Boolean.valueOf(property);
    }
}
