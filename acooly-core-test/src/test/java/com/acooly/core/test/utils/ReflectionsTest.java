package com.acooly.core.test.utils;

import com.acooly.core.test.domain.App;
import com.acooly.core.test.utils.bean.MaskNode;
import com.acooly.core.utils.Reflections;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author zhangpu
 * @date 2019-05-23 11:51
 */
@Slf4j
public class ReflectionsTest {

    @Test
    public void testGetParameterGenericType() {
        MaskNode maskNode = new MaskNode(1L, null, "TOP");
        Field filed = Reflections.getAccessibleField(maskNode, "children");
        log.info("filed.getDeclaringClass(): {}", filed.getDeclaringClass());
        log.info("ParameterGenericType:{}", Reflections.getParameterGenericType(filed));
    }

    @Test
    public void testCreateObject() throws Exception {
        App app = Reflections.createObject(App.class);
        log.info("CreateObject:{}", app.toString());
    }


}
