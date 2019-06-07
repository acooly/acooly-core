/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils.mapper;

import com.acooly.core.utils.BeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 *
 * <p>1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 *
 * @author calvin
 * @author zhangpu 请使用BeanCopier
 */
@Deprecated
public class BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }

    public static DozerBeanMapper getDozer() {
        return dozer;
    }

    @Deprecated
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> deepMap(Object source, String[] properties) throws Exception {

        Map<String, Object> map = Maps.newHashMap();
        for (String property : properties) {
            if (StringUtils.contains(property, ".")) {
                String currentProperty = StringUtils.substringBefore(property, ".");
                String remainProperties = StringUtils.substringAfter(property, ".");
                Map<String, Object> remainMap =
                        deepMap(
                                BeanUtils.getDeclaredProperty(source, currentProperty),
                                new String[]{remainProperties});
                if (map.get(currentProperty) != null) {
                    ((Map) map.get(currentProperty)).putAll(remainMap);
                } else {
                    map.put(currentProperty, remainMap);
                }

            } else {
                Object value = BeanUtils.getDeclaredProperty(source, property);
                if (value instanceof Collection) {
                }

                map.put(property, value);
            }
        }
        return map;
    }
}
