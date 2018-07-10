/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-07-05 15:36 创建
 */
package com.acooly.module.distributedlock.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shuijing
 */
@Slf4j
public class RedisLockEntityHolder {

    public static RedisLockEntityHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RedisLockEntityHolder INSTANCE = new RedisLockEntityHolder();
    }

    private final ConcurrentMap<String, RedisLockEntry> entries = newConcurrentHashMap();

    private final ConcurrentMap<String, RedisMessageListenerContainer> delListeners = newConcurrentHashMap();


    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public RedisLockEntry getEntry(String entryName) {
        return entries.get(entryName);
    }


    public void registEntry(final String entryName) {

        RedisLockEntry entry = entries.get(entryName);
        if (entry != null) {
            entry.aquire();
            return;
        }

        RedisLockEntry value = new RedisLockEntry();
        value.aquire();

        RedisLockEntry oldValue = entries.putIfAbsent(entryName, value);
        if (oldValue != null) {
            oldValue.aquire();
        }
    }

    //订阅渠道：unlock事件
    public void subscribeTopic(final String entryName, final String channelName, RedisTemplate redisTemplate, RedisLockEntry redisLockEntry) {

        if (delListeners.containsKey(entryName)) {
            return;
        }

        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());

        //监听事件
        //todo listener add
        container.addMessageListener(new MessageListenerAdapter((MessageListener) (message, pattern) -> {
            byte[] channel = message.getChannel();
            byte[] body = message.getBody();
            String msg = new String(body);
            String c = new String(channel);
            log.debug("收到{}消息:{}", c, msg);
            // 释放等待的锁
            if (StringUtils.isNotEmpty(msg)) {
                redisLockEntry.getLatch().release();
            }
        }), new ChannelTopic(channelName));

        //启动监听
        container.afterPropertiesSet();
        container.start();

        log.debug("订阅消息，channelName:{},entryName:{}", channelName, entryName);

        delListeners.putIfAbsent(entryName, container);
    }

    //取消订阅
    public void unSubscribeTopic(final String entryName) {
        RedisMessageListenerContainer listenerContainer = delListeners.remove(entryName);
        if (listenerContainer != null) {
            listenerContainer.stop();
        }
        log.debug("停止订阅消息，channelName:{},entryName:{}" , entryName);

    }
}
