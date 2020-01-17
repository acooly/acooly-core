/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-06-17 17:51 创建
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.ObjectId;
import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Ids分布式Id单元测试
 *
 * @author zhangpu@acooly.cn
 */
@Slf4j
public class IdsTest {

    @Test
    public void testObjectId() {
        String id = ObjectId.get().toHexString();
        log.info("ObjectId {}, length: {}", id, Strings.length(id));


    }

}
