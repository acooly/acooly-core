/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.enums;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 统一枚举接口
 *
 * @author zhangpu
 */
public interface Messageable extends Serializable {

    /**
     * 编码
     *
     * @return
     */
    String code();

    /**
     * 消息
     *
     * @return
     */
    String message();


    /**
     * 专用于Exception的国际化消息
     * <p>
     * Key直接未code
     *
     * @return
     */
    default String i18nErrorMessage() {
        return i18nMessage(null);
    }

    /**
     * 快捷（顺便）获取异常的detail的国际化值
     *
     * @param detailKey
     * @param detailDefault
     * @return
     */
    default String i18nErrorMessage(String detailKey, String detailDefault) {
        return getI18n(detailKey, detailDefault);
    }

    /**
     * 反射方式获取code对应的国际化消息
     * <p>
     * 使用条件：
     * 1. Spring容器环境内
     * 2. 依赖`acooly-component-i18n`组件，判断I18nMessages是否存在，如果存在则调用getMessage方法获取国际化消息
     * 3. 国际化消息的key为：{枚举类全路径}.{枚举值code}
     * 4. 对应通用的枚举，该方法只能用于其一的定义（例如：SimpleStatus）
     *
     * @return
     */
    default String i18nMessage() {
        return i18nMessage(this.getClass().getPackage().getName());
    }

    /**
     * 指定key前缀的国际化消息
     *
     * @param keyPrefix
     * @return
     */
    default String i18nMessage(String keyPrefix) {
        return i18nMessage(keyPrefix + "." + this.getClass().getSimpleName(), null);
    }

    /**
     * 指定前缀和参数的国际化消息
     *
     * @param keyPrefix
     * @param args
     * @return
     */
    default String i18nMessage(String keyPrefix, String... args) {
        // 未开启则直接返回message
        boolean i18nEnable = Boolean.parseBoolean(System.getProperty("acooly.i18n.enable"));
        if (!i18nEnable) {
            return message();
        }
        // 前缀处理
        String i18nKey = code();
        if (keyPrefix != null && !keyPrefix.isEmpty()) {
            i18nKey = keyPrefix + "." + i18nKey;
        }
        String message = getI18n(i18nKey, message());
        // 如果有参数，则格式化消息
        if (args != null && args.length > 0) {
            message = String.format(message, args);
        }
        return message;
    }

    static String getI18n(String code, String defaultValue) {
        // 未开启则直接返回message
        boolean i18nEnable = Boolean.parseBoolean(System.getProperty("acooly.i18n.enable"));
        if (!i18nEnable) {
            return defaultValue;
        }
        String className = "com.acooly.component.i18n.support.I18nMessages";
        String methodName = "getMessage";
        String message = defaultValue;
        try {
            Class clazz = Class.forName(className);
            // 根据方法名称和参数列表获取方法对象
            Method method = clazz.getDeclaredMethod(methodName, new Class[]{String.class});
            // 设置访问权限为true（如果需要）
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            message = (String) method.invoke(null, code);
        } catch (Exception e) {
            // ignore
        }
        return message;
    }


}
