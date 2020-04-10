/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils.mapper;

import com.acooly.core.utils.BeanUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 因cms组件有用到，保留此方法
 */
@Deprecated
public class BeanMapper {

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
