/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-25 18:30 创建
 */
package com.acooly.core.common.boot.component.cache;

import com.acooly.core.utils.kryos.Kryos;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author qiubo
 */
public class DefaultRedisSerializer implements RedisSerializer {

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return Kryos.serialize(o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return Kryos.deserialize(bytes);
    }
}
