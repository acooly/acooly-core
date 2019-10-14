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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * @author qiubo
 */
public class EnvironmentHolder implements EnvironmentAware {
    private static Environment environment;

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
     * 通过环境配置构建配置对象,配置对象必须标注{@link ConfigurationProperties},使用ConfigurationProperties中的prefix填充对象
     *
     * @param target 配置对象
     */
    public static void buildProperties(Object target) {
        PropertiesBuilder.build(target);
    }

    public static <T> T buildProperties(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return (T) PropertiesBuilder.build(t);
        } catch (Exception e) {
            throw new AppConfigException(e);
        }
    }

    /**
     * 通过环境配置构建配置对象,配置对象必须标注{@link ConfigurationProperties}
     *
     * @param target 配置对象
     * @param prefix 配置前缀
     */
    public static Object buildProperties(Object target, String prefix) {
        return PropertiesBuilder.build(target, prefix);
    }

    @Override
    public void setEnvironment(Environment environment) {
        if (EnvironmentHolder.environment == null) {
            if (environment != null) {
                set(environment);
            }
        }
    }

    private static class PropertiesBuilder {

        public static Object build(Object target, String prefix) {
            Assert.notNull(target, "target对象不能为空");
            Assert.notNull(prefix, "prefix不能为空");
            try {


                BindResult bindResult = Binder.get(EnvironmentHolder.get()).bind(prefix, Bindable.ofInstance(target));
                if (bindResult.isBound()) {
                    target = bindResult.get();
                }
                if (target instanceof InitializingBean) {
                    ((InitializingBean) target).afterPropertiesSet();
                }
            } catch (Exception e) {
                throw new AppConfigException(target.getClass().getSimpleName() + "配置对象初始化失败", e);
            }
            return target;
        }

        public static Object build(Object target) {
            Assert.notNull(target, "target对象不能为空");
            ConfigurationProperties properties =
                    AnnotationUtils.findAnnotation(target.getClass(), ConfigurationProperties.class);
            Assert.notNull(properties, "target对象必须标注ConfigurationProperties注解");

            String prefix = properties.prefix();
            if (Strings.isNullOrEmpty(prefix)) {
                prefix = properties.value();
            }
            return build(target, prefix);
        }
    }
}
