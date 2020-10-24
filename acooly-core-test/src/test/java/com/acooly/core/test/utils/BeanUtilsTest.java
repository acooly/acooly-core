/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 19:47
 */
package com.acooly.core.test.utils;

import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.utils.bean.MaskEntity;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.Reflections;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @author zhangpu
 * @date 2019-09-01 19:47
 */
@Slf4j
public class BeanUtilsTest {

    @Test
    public void testGetAccessor() {
        System.out.println(BeanUtils.getAccessorName(App.class, "displayName"));
        System.out.println(BeanUtils.getAccessorName(App.class, "enable"));
    }

    @Test
    public void testCopyToList() {
        MaskEntity entity = new MaskEntity();
        Set<Field> fields = Reflections.getFields(entity.getClass());
        List<Field> fieldList = Lists.newArrayList(fields);
        log.info("原始顺序：{}", fieldList);
        AnnotationAwareOrderComparator.sort(fieldList);
        List<Object> values = Lists.newArrayList();
        for (Field field : fieldList) {
            values.add(Reflections.getFieldValue(entity, field));
        }
        log.info("排序顺序：{}", fieldList);
        log.info("排序值：{}", values);
    }

}
