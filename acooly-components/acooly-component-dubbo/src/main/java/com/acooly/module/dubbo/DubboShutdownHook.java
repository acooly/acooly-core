/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:51 创建
 */
package com.acooly.module.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author qiubo@yiji.com
 */
public class DubboShutdownHook implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DubboShutdownHook.class);
    private static volatile boolean run = false;

    @Override
    public void run() {
        if (run) {
            return;
        }
        logger.info("关闭dubbo");
        run = true;
        try {
            final Class protocolConfig = Class.forName("com.alibaba.dubbo.config.ProtocolConfig");
            final Method method = protocolConfig.getMethod("destroyAll");
            try {
                method.invoke(protocolConfig);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Throwable e) {
            //ignore
        }
    }
}
