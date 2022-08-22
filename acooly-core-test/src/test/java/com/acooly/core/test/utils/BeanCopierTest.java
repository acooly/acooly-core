/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-31 23:39
 */
package com.acooly.core.test.utils;

import com.acooly.core.test.utils.bean.MaskEntity;
import com.acooly.core.utils.mapper.BeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2019-10-31 23:39
 */
@Slf4j
public class BeanCopierTest {


    @Test
    public void testSimpleBeanCopy() {

        MaskEntity entity1 = new MaskEntity();
        entity1.setBankCardNo("11111111");
        BeanCopier.logSource = true;
        MaskEntity entity2 = BeanCopier.copy(entity1, MaskEntity.class);
        log.info("entity2:{}", entity2.toString());
        log.info(MaskEntity.class.getName());
    }
}
