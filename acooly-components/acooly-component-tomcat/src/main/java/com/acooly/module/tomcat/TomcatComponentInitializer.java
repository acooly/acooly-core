/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-10 18:47 创建
 *
 */
package com.acooly.module.tomcat;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo
 */
public class TomcatComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 奇葩的阿里云slb不在内网ip段
        setPropertyIfMissing("server.tomcat.remoteip.internal-proxies", ".*");
        setPropertyIfMissing("server.tomcat.remoteip.remote-ip-header", "x-forwarded-for");
        setPropertyIfMissing("server.tomcat.remoteip.protocol-header", "x-forwarded-proto");
        // 没必要，默认就是
        setPropertyIfMissing("server.tomcat.uri-encoding", "UTF-8");
        // 兼容老版本
        if (applicationContext.getEnvironment().getProperty("acooly.tomcat.port") != null) {
            System.setProperty(Apps.HTTP_PORT, applicationContext.getEnvironment().getProperty("acooly.tomcat.port"));
        }
    }
}
