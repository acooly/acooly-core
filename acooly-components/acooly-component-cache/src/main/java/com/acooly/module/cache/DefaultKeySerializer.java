/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-26 10:08 创建
 */
package com.acooly.module.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author qiubo@yiji.com
 */
public class DefaultKeySerializer implements RedisSerializer<Object> {
    private final Charset charset = Charset.forName("UTF8");

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return null;
        } else {
            return o.toString().getBytes(charset);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}