/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:52 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.utils.ShutdownHooks;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class DubboRemoteProxyFacotry implements ApplicationContextAware {
    private static final Logger logger =
            LoggerFactory.getLogger(DubboRemoteProxyFacotry.class.getName());
    private static final int PROVIDER_TIME_OUT = -1;
    private static volatile Map<Key, ReferenceConfig> cache = Maps.newConcurrentMap();

    static {
        ShutdownHooks.addShutdownHook(
                new Runnable() {
                    @Override
                    public void run() {
                        for (ReferenceConfig referenceConfig : cache.values()) {
                            try {
                                referenceConfig.destroy();
                            } catch (Exception e) {
                                logger.error("{}销毁异常", referenceConfig);
                            }
                        }
                        cache.clear();
                    }
                },
                "DubboRemoteProxyFacotryShutdownHook");
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获得服务调用
     *
     * @param clazz   服务接口类
     * @param group   服务组名
     * @param version 服务版本
     * @param <T>     服务接口
     * @param timeout 服务调用超时时间 单位ms
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz, String group, String version, int timeout) {
        if (Strings.isNullOrEmpty(version) || clazz == null) {
            throw new RuntimeException("获取dubbo服务代理时版本号和类不能为空");
        }
        Key key = new Key(clazz, group, version);
        ReferenceConfig cachedReferenceConfig = cache.get(key);
        if (cachedReferenceConfig != null) {
            return (T) cachedReferenceConfig.get();
        } else {
            if (this.applicationContext == null) {
                throw new RuntimeException("请配置DubboRemoteProxyFacotry到spring容器中");
            }
            synchronized (DubboRemoteProxyFacotry.class) {
                cachedReferenceConfig = cache.get(key);
                if (cachedReferenceConfig == null) {
                    ApplicationConfig applicationConfig = applicationContext.getBean(ApplicationConfig.class);
                    Map<String, RegistryConfig> registryConfigMap =
                            applicationContext.getBeansOfType(RegistryConfig.class);
                    if (registryConfigMap == null || registryConfigMap.isEmpty()) {
                        throw new RuntimeException("请配置dubbo基本配置");
                    }
                    ReferenceConfig reference = new ReferenceConfig();
                    reference.setApplication(applicationConfig);
                    reference.setRegistries(new ArrayList<>(registryConfigMap.values()));
                    reference.setInterface(clazz);
                    reference.setVersion(version);
                    reference.setGroup(group);
                    if (PROVIDER_TIME_OUT != timeout) {
                        reference.setTimeout(timeout);
                    }
                    try {
                        //init
                        reference.get();
                    } catch (Exception e) {
                        logger.error("获取dubbo服务失败:{}", e.getMessage());
                        try {
                            reference.destroy();
                        } catch (Exception e1) {
                            logger.error("获取dubbo服务失败,销毁Invoker失败:{}", e1.getMessage());
                            throw new RuntimeException(
                                    "获取dubbo服务失败,销毁Invoker失败:" + e.getMessage() + ",cause:" + e.getMessage());
                        }
                        throw new RuntimeException("获取dubbo服务失败:" + e.getMessage());
                    }
                    cache.put(key, reference);
                    cachedReferenceConfig = reference;
                }
                return (T) cachedReferenceConfig.get();
            }
        }
    }

    /**
     * 获得服务代理,调用超时时间以服务提供方设置为准
     *
     * @param clazz   服务接口类
     * @param group   服务组名
     * @param version 服务版本
     * @param <T>     服务接口
     * @return dubbo服务代理对象
     */
    public <T> T getProxy(Class<T> clazz, String group, String version) {
        return this.getProxy(clazz, group, version, PROVIDER_TIME_OUT);
    }

    private static class Key {
        private Class<?> clazz;
        private String group;
        private String version;

        private Key(Class<?> clazz, String group, String version) {
            this.clazz = clazz;
            this.group = group;
            this.version = version;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (clazz != null ? !clazz.equals(key.clazz) : key.clazz != null) return false;
            if (group != null ? !group.equals(key.group) : key.group != null) return false;
            if (version != null ? !version.equals(key.version) : key.version != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (group != null ? group.hashCode() : 0);
            result = 31 * result + (version != null ? version.hashCode() : 0);
            return result;
        }
    }
}
