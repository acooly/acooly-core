/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-08 10:39
 */
package com.acooly.core.utils;

import com.acooly.core.common.exception.ParameterException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具栏
 * <p>
 * 直接转换为BusinessException，免去调用端的异常处理和转换
 *
 * @author zhangpu
 * @date 2019-06-08
 */
@Slf4j
public class Asserts {

    /**
     * 判断数字（int）在start（含）和end（含）间
     *
     * @param paramValue 参数值
     * @param start      最小值
     * @param end        最大值
     * @param paramName  参数名
     * @param message    消息
     */
    public static void between(int paramValue, int start, int end, String paramName, String message) {
        if (paramValue < start || paramValue > end) {
            throw new ParameterException(paramName, message);
        }
    }

    /**
     * 判断数字（long）在start（含）和end（含）间
     *
     * @param paramValue 参数值
     * @param start      最小值
     * @param end        最大值
     * @param paramName  参数名
     * @param message    消息
     */
    public static void between(long paramValue, long start, long end, String paramName, String message) {
        if (paramValue < start || paramValue > end) {
            throw new ParameterException(paramName, message);
        }
    }

    /**
     * 字符串不能为空及异常
     *
     * @param text      参数值（字符串）
     * @param message   错误消息
     * @param paramName 参数名称
     * @see Strings#isBlank(CharSequence)
     */
    public static void notEmpty(String text, String paramName, String message) {
        if (Strings.isBlank(text)) {
            throw new ParameterException(paramName, message);
        }
    }

    /**
     * 字符串不能为空判断及异常
     * <p>
     * 采用默认消息
     *
     * @param text      参数值（字符串）
     * @param paramName 参数名称
     */
    public static void notEmpty(String text, String paramName) {
        notEmpty(text, paramName, "%s不能为空和空字符串");
    }

    /**
     * 字符串不能为空判断及异常
     *
     * @param text
     */
    public static void notEmpty(String text) {
        notEmpty(text, null);
    }


    /**
     * 字符串为空断言
     * 判断字符串为空，如果不为空，则抛出异常
     *
     * @param text      参数值（字符串）
     * @param paramName 参数名称
     * @param message   错误消息
     */
    public static void empty(String text, String paramName, String message) {
        if (Strings.isNotBlank(text)) {
            throw new ParameterException(paramName, message);
        }
    }

    /**
     * 字符串为空断言
     * 默认错误消息
     *
     * @param text      参数值（字符串）
     * @param paramName 参数名称
     */
    public static void empty(String text, String paramName) {
        empty(text, paramName, "%s必须为空或空字符串");
    }

    /**
     * 字符串为空断言
     *
     * @param text 参数值（字符串）
     */
    public static void empty(String text) {
        empty(text, null);
    }

    /**
     * 集合不为空断言
     *
     * @param collection
     * @param paramName
     * @param message
     */
    public static void notEmpty(Collection<?> collection, String paramName, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void notEmpty(Collection<?> collection, String paramName) {
        notEmpty(collection, paramName, "%s集合不能为空，并至少有一个成员");
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, null);
    }

    /**
     * Map不为空断言
     *
     * @param map
     * @param paramName
     * @param message
     */
    public static void notEmpty(Map<?, ?> map, String paramName, String message) {
        if (map == null || map.isEmpty()) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String paramName) {
        notEmpty(map, paramName, "%s不能为空，并至少有一个成员");
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, null);
    }

    /**
     * 数组不为空断言
     *
     * @param array
     * @param paramName
     * @param message
     */
    public static void notEmpty(Object[] array, String paramName, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void notEmpty(Object[] array, String paramName) {
        notEmpty(array, paramName, "%s不能为空，并至少有一个成员");
    }

    public static void notEmpty(Object[] array) {
        notEmpty(array, null);
    }

    public static void isTrue(boolean expression, String paramName, String message) {
        if (!expression) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void isTrue(boolean expression, String paramName) {
        isTrue(expression, paramName, "%s值或表达式必须为true");
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, null);
    }

    public static void isNull(Object object, String paramName, String message) {
        if (object == null) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void isNull(Object object, String paramName) {
        isNull(object, paramName, "%s必须为空");
    }

    public static void isNull(Object object) {
        isNull(object, null);
    }


    public static void notNull(Object object, String paramName, String message) {
        if (object == null) {
            throw new ParameterException(paramName, message);
        }
    }

    public static void notNull(Object object, String paramName) {
        if (Strings.isBlank(paramName)) {
            paramName = "参数";
        }
        notNull(object, paramName, "%s不能为空");
    }

    public static void notNull(Object object) {
        notNull(object, null);
    }
}
