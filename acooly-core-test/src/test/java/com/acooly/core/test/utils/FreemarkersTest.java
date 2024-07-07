/**
 * acooly-core-5.2
 * <p>
 * Copyright 2024 acooly.com, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2024-07-03
 */
package com.acooly.core.test.utils;

import com.acooly.core.test.common.AppEntity;
import com.acooly.core.utils.FreeMarkers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2024-07-03
 */
@Slf4j
public class FreemarkersTest {

    @Test
    public void testMuiltiple() {

        String templateString = "这是测试模板，第一个渲染的是对象的属性name: ${name}, 第二个渲染的参数是Map传入的key,当前时间:${currentDateTime?string('yyyy-MM-dd HH:mm:ss')}";

        AppEntity appEntity = new AppEntity();
        appEntity.setName("王麻子");
        String result = FreeMarkers.rendereString(templateString, appEntity);
        log.info("result:{}", result);


    }
}
