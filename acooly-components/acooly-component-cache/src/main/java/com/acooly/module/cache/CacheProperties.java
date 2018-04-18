/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-24 00:11 创建
 */
package com.acooly.module.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = "acooly.cache")
@Data
public class CacheProperties {

    /**
     * 可选：基于注解的Spring CacheManager，设置缓存的过期时间。默认为0，即不会过期
     * 如果是使用RedisTemplate来显示读写缓存的，需要自己调用expire方法设置每个key的过期时间 单位秒
     */
    private int expireTime = 3600;
}
