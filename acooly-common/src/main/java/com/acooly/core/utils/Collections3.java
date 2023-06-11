/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collections工具集.
 *
 * <p>在JDK的Collections和Guava的Collections2后, 命名为Collections3.
 *
 * <p>函数主要由两部分组成，一是自反射提取元素的功能，二是源自Apache Commons Collection, 争取不用在项目里引入它。
 *
 * @author calvin
 * @author zhangpu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Collections3 {


    /**
     * 获取集合的Top列表
     *
     * @param collection 支持List和Set等集合
     * @param limit      top数量
     * @param <T>        集合的成员泛型
     * @return top数量的集合列表
     */
    public static <T> Collection<T> top(Collection<T> collection, int limit) {
        if (isEmpty(collection) || collection.size() <= limit) {
            return collection;
        }
        if (collection instanceof List) {
            return collection.stream().limit(limit).collect(Collectors.toList());
        } else {
            return collection.stream().limit(limit).collect(Collectors.toSet());
        }
    }


    /**
     * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
     *
     * @param collection        来源集合.
     * @param keyPropertyName   要提取为Map中的Key值的属性名.
     * @param valuePropertyName 要提取为Map中的Value值的属性名.
     * @return Map对象
     */
    public static Map extractToMap(final Collection collection, final String keyPropertyName, final String valuePropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName), PropertyUtils.getProperty(obj, valuePropertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return map;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @return 对象单个属性的List对象
     */
    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     * @return 分隔符分割的字符串
     */
    public static String extractToString(final Collection collection, final String propertyName, final String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     *
     * @param collection 来源集合.
     * @param separator  分隔符.
     * @return 分隔符分割的字符串
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
     *
     * @param collection 来源集合.
     * @param prefix     每个元素的前缀
     * @param postfix    每个元素的后缀
     * @return 前后缀分割的字符串
     */
    public static String convertToString(final Collection collection, final String prefix, final String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }

    /**
     * 判断集合是否为空.
     *
     * @param collection 集合
     * @return 是否为空 ，true：空，false：不为空
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断是否不为为空.
     *
     * @param collection 集合
     * @return 是否不为空 ，true：不为空，false：为空
     */
    public static boolean isNotEmpty(Collection collection) {
        return (collection != null && !(collection.isEmpty()));
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     *
     * @param collection 集合
     * @param <T>        集合成员泛型
     * @return 第一个元素或null
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素 ，如果collection为空返回null.
     *
     * @param collection 集合
     * @param <T>        集合成员泛型
     * @return 最后一个元素或null
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        // 当类型为List时，直接取得最后一个元素 。
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        // 其他类型通过iterator滚动到最后一个元素.
        Iterator<T> iterator = collection.iterator();
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    /**
     * 合并集合（为List）
     * 注意不会去出重复
     *
     * @param a   被合并的集合a
     * @param b   被合并的集合b
     * @param <T> 集合成员枚举类型
     * @return 合并后的List集合
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 集合减法
     * 从集合a中去除集合b的成员，返回a-b的新List.
     *
     * @param a   源集合
     * @param b   被剔除的成员集合
     * @param <T> 成员泛型
     * @return 去除指定成员的集合
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<T>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 集合交集
     * 返回a与b的交集的新List.
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 集合泛型
     * @return a和b的交集（都存在的元素）
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<T>();

        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }
}
