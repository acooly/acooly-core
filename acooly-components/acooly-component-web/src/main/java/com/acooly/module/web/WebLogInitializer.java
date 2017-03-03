/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-03 11:34 创建
 */
package com.acooly.module.web;

import ch.qos.logback.classic.Level;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;

/**
 * @author qiubo@yiji.com
 */
public class WebLogInitializer extends AbstractLogInitializer {
    @Override
    public void init(LogbackConfigurator configurator) {
        if(!Apps.isDevMode()){
            configurator.logger("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping", Level.WARN);
            configurator.logger("org.springframework.web.servlet.handler.SimpleUrlHandlerMapping", Level.WARN);
        }
    }
}