package com.acooly.core.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 常用类型工具
 *
 * @author zhangpu
 * @date 2019-05-23 12:44
 */
@Slf4j
public class Types {

    public static final List<Class<? extends Serializable>> BASE_TYPES = Lists.newArrayList(
            String.class, Character.class, Boolean.class, Date.class, Number.class
    );


    public static boolean isJavaBean(Class<?> clazz) {
        if (isBaseType(clazz) || isMoney(clazz) || isEnum(clazz) ||
                isArray(clazz) || isCollection(clazz) || isMap(clazz)) {
            return false;
        }
        return true;
    }

    public static boolean isBaseType(Class<?> clazz) {
        if (isPrimitive(clazz)) {
            return true;
        }

        for (Class<? extends Serializable> cls : BASE_TYPES) {
            if (cls.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    public static boolean isDate(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    public static boolean isString(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    public static boolean isMoney(Class<?> clazz) {
        return Money.class.isAssignableFrom(clazz);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();
    }

    public static boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz)
                || (clazz.isPrimitive() && "long,int,byte,short".contains(clazz.toString()));
    }
}
