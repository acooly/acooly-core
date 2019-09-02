/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 19:47
 */
package com.acooly.core.test.utils;

import com.acooly.core.test.domain.App;
import com.acooly.core.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2019-09-01 19:47
 */
@Slf4j
public class BeanUtilsTest {

    @Test
    public void testGetAccessor() {

        System.out.println(BeanUtils.getAccessorName(App.class,"displayName"));
        System.out.println(BeanUtils.getAccessorName(App.class,"enable"));
    }
}
