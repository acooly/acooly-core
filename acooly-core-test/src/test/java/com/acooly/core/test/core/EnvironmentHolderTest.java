/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-14 08:24
 */
package com.acooly.core.test.core;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.module.dubbo.DubboProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;

/**
 * @author zhangpu
 * @date 2019-10-14 08:24
 */
@Slf4j
public class EnvironmentHolderTest {


    @Test
    public void testBind() {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        DubboProperties properties = new DubboProperties();
        EnvironmentHolder.buildProperties(properties);
        System.out.println(properties);
    }

}
