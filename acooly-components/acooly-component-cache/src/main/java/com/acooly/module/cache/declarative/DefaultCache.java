/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-11-05 07:04 创建
 */
package com.acooly.module.cache.declarative;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * @author qiubo
 */
public class DefaultCache implements Cache {

    private String name;
    private long expiration;
    private RedisConnectionFactory connectionFactory;
    private RedisTemplate redisTemplate;
    private RedisSerializer valueRedisSerializer;
    private byte[] prefix;

    public DefaultCache(RedisTemplate redisTemplate, String name, long expiration) {
        this.connectionFactory = redisTemplate.getConnectionFactory();
        this.name = name;
        this.expiration = expiration;
        this.redisTemplate = redisTemplate;
        this.valueRedisSerializer = redisTemplate.getValueSerializer();
        this.prefix = CacheKeyPrefix.simple().compute(name).getBytes();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueWrapper get(Object key) {
        if (key == null) {
            return null;
        }
        return (ValueWrapper)
                doit(
                        key,
                        (conn, newkey) -> {
                            byte[] bytes = conn.get(newkey);
                            if (bytes == null) {
                                return null;
                            }
                            return (ValueWrapper) () -> valueRedisSerializer.deserialize(bytes);
                        });
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = this.get(key);
        return wrapper == null ? null : (T) wrapper.get();
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(Object key, Object value) {
        if (key == null || value == null) {
            return;
        }
        doit(
                key,
                (conn, newkey) -> {
                    conn.set(
                            newkey, valueRedisSerializer.serialize(value), Expiration.seconds(expiration), SetOption.UPSERT);
                    return null;
                });
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evict(Object key) {
        if (key == null) {
            return;
        }
        doit(key, (conn, newkey) -> conn.del(newkey));
    }

    public Object doit(Object key, BiFunction<RedisConnection, byte[], Object> fun) {
        RedisConnection connection = null;
        try {
            connection = connectionFactory.getConnection();
            byte[] newkey = computeKey(key);
            return fun.apply(connection, newkey);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private byte[] computeKey(Object key) {
        byte[] keyBytes = this.convertToBytesIfNecessary(this.redisTemplate.getKeySerializer(), key);
        if (this.prefix != null && this.prefix.length != 0) {
            byte[] result = Arrays.copyOf(this.prefix, this.prefix.length + keyBytes.length);
            System.arraycopy(keyBytes, 0, result, this.prefix.length, keyBytes.length);
            return result;
        } else {
            return keyBytes;
        }
    }

    private byte[] convertToBytesIfNecessary(RedisSerializer<Object> serializer, Object value) {
        return value instanceof byte[]
                ? (byte[]) (value)
                : (null == serializer ? null : serializer.serialize(value));
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
