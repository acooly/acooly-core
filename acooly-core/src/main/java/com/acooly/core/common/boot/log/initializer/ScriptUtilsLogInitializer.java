/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 23:16 创建
 */
package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.Level;
import com.acooly.core.common.boot.log.LogbackConfigurator;

/**
 * @author qiubo@yiji.com
 */
public class ScriptUtilsLogInitializer extends AbstractLogInitializer {
    @Override
    public void init(LogbackConfigurator configurator) {
        configurator.logger("org.springframework.jdbc.datasource.init.ScriptUtils", Level.WARN);
    }
}
