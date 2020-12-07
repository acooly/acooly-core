/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-12-07 22:41
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Regexs;
import com.acooly.core.utils.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2020-12-07 22:41
 */
@Slf4j
public class ServletsTest {

    @Test
    public void testGetServerRoot() {
        String url1 = "http://www.acooly.cn/docs/core.html";
        log.info("{} - root -> {}", url1, Servlets.getServerRoot(url1));
        String url2 = "https://www.acooly.cn/docs/core.html";
        log.info("{} - root -> {}", url2, Servlets.getServerRoot(url2));
        String url3 = "https://acooly.cn/docs/component.html?type=gateway";
        log.info("{} - root -> {}", url3, Servlets.getServerRoot(url3));
        String url4 = "https://acooly.cn:8099/docs/component.html?type=gateway";
        log.info("{} - root -> {}", url4, Servlets.getServerRoot(url4));
        String url5 = "ftp://zhangpu:123456@acooly.cn:8099/docs/component.html?type=gateway";
        log.info("{} - root -> {}", url5, Servlets.getServerRoot(url5));
    }

}
