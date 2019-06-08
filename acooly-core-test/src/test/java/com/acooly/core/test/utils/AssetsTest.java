/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-08 11:01
 */
package com.acooly.core.test.utils;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Assets;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2019-06-08 11:01
 */
@Slf4j
public class AssetsTest {

    @Test
    public void testNotEmpty() {
        String realName = null;
        try {
            Assets.notEmpty(realName, "姓名", "姓名不能为空");
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
        try {
            Assets.notEmpty(realName, "姓名");
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
        try {
            Assets.notEmpty(realName);
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
    }

    @Test
    public void testEmpty() {
        String realName = "张飞";
        try {
            Assets.empty(realName, "姓名", "姓名必须为空");
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
        try {
            Assets.empty(realName, "姓名");
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
        try {
            Assets.empty(realName);
        } catch (BusinessException be) {
            log.warn("Asset fail: {}", be.getMessage());
        }
    }
}
