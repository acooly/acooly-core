package com.acooly.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

/**
 * 常用类型工具
 *
 * @author zhangpu
 * @date 2019-05-23 12:44
 */
@Slf4j
public class Types {

    public static final List<Class<? extends Serializable>> BASE_TYPES = new ArrayList<>();

    static {
        BASE_TYPES.add(String.class);
        BASE_TYPES.add(Number.class);
        BASE_TYPES.add(Character.class);
        BASE_TYPES.add(Boolean.class);
        BASE_TYPES.add(Date.class);
    }


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
