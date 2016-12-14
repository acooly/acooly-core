/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 22:36 创建
 */
package com.acooly.core.common.boot;

import com.acooly.core.common.exception.AppConfigException;
import com.google.common.base.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * @author qiubo@yiji.com
 */
public class EnvironmentHolder implements EnvironmentAware {
    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        if (EnvironmentHolder.environment == null) {
            if (environment != null) {
                set(environment);
            }
        }
    }

    private static void set(Environment env) {
        environment = env;
    }

    /**
     * 获取环境
     */
    public static Environment get() {
        return environment;
    }

    /**
     * 获取RelaxedPropertyResolver
     * @param prefix 前缀
     * @return RelaxedPropertyResolver
     */
    public static RelaxedPropertyResolver get(String prefix) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(EnvironmentHolder.get(), prefix);
        return resolver;
    }

    public static class RelaxedProperty {
        private String prefix;
        private String key;

        /**
         *
         * @param prefix 配置项前缀
         * @param key 配置项key
         */
        public RelaxedProperty(String prefix, String key) {
            Assert.notNull(prefix);
            Assert.notNull(key);
            this.key = key;
            if (!prefix.endsWith(".")) {
                prefix = prefix + ".";
            }
            this.prefix = prefix;
        }

        public String getKey() {
            return key;
        }

        public String getPrefix() {
            return prefix;
        }

        /**
         * 获取此配置项的配置名
         */
        public String getPropertyName() {
            return this.prefix + this.key;
        }

        /**
         * 从环境中获取配置项的值
         */
        public String getProperty() {
            return getProperty((String) null);
        }

        /**
         * 从环境中获取配置项的值,如果值为空则返回默认值
         *
         * @param defaultValue 默认值
         */
        public String getProperty(String defaultValue) {
            return EnvironmentHolder.get(this.prefix).getProperty(this.key, defaultValue);
        }

        /**
         * 从环境中获取配置项的值,并转换为目标类型
         * @param targetType 目标类型
         */
        public <T> T getProperty(Class<T> targetType) {
            return getProperty(targetType, null);
        }

        /**
         * 从环境中获取配置项的值,并转换为目标类型
         * @param targetType 目标类型
         * @param defaultValue 默认值
         */
        public <T> T getProperty(Class<T> targetType, T defaultValue) {
            return EnvironmentHolder.get(this.prefix).getProperty(this.key, targetType, defaultValue);

        }
    }

    /**
     * 通过环境配置构建配置对象,配置对象必须标注{@link ConfigurationProperties},使用ConfigurationProperties中的prefix填充对象
     * @param target 配置对象
     */
    public static void buildProperties(Object target) {
        PropertiesBuilder.build(target);
    }

    /**
     * 通过环境配置构建配置对象,配置对象必须标注{@link ConfigurationProperties}
     * @param target 配置对象
     * @param prefix 配置前缀
     */
    public static void buildProperties(Object target, String prefix) {
        PropertiesBuilder.build(target, prefix);
    }

    private static class PropertiesBuilder {
        private static DefaultConversionService defaultConversionService = new DefaultConversionService();

        public static void build(Object target, String prefix) {
            Assert.notNull(target, "target对象不能为空");
            Assert.notNull(prefix, "prefix不能为空");
            ConfigurationProperties properties = AnnotationUtils.findAnnotation(target.getClass(),
                    ConfigurationProperties.class);
            boolean ignoreInvalidFields = properties.ignoreInvalidFields();
            boolean ignoreNestedProperties = properties.ignoreNestedProperties();
            boolean ignoreUnknownFields = properties.ignoreUnknownFields();
            boolean exceptionIfInvalid = properties.exceptionIfInvalid();

            PropertiesConfigurationFactory<Object> configurationFactory = new PropertiesConfigurationFactory<>(target);
            configurationFactory.setPropertySources(((ConfigurableEnvironment) (EnvironmentHolder.get()))
                    .getPropertySources());
            configurationFactory.setConversionService(defaultConversionService);
            configurationFactory.setTargetName(prefix);
            configurationFactory.setIgnoreInvalidFields(ignoreInvalidFields);
            configurationFactory.setIgnoreNestedProperties(ignoreNestedProperties);
            configurationFactory.setIgnoreUnknownFields(ignoreUnknownFields);
            configurationFactory.setExceptionIfInvalid(exceptionIfInvalid);
            try {
                configurationFactory.bindPropertiesToTarget();
                if (target instanceof InitializingBean) {
                    ((InitializingBean) target).afterPropertiesSet();
                }
            } catch (Exception e) {
                throw new AppConfigException(target.getClass().getSimpleName() + "配置对象初始化失败", e);
            }
        }

        public static void build(Object target) {
            Assert.notNull(target, "target对象不能为空");
            ConfigurationProperties properties = AnnotationUtils.findAnnotation(target.getClass(),
                    ConfigurationProperties.class);
            Assert.notNull(properties, "target对象必须标注ConfigurationProperties注解");

            String prefix = properties.prefix();
            if (Strings.isNullOrEmpty(prefix)) {
                prefix = properties.value();
            }
            build(target, prefix);
        }
    }
}
