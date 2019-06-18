/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-18 15:01 创建
 *
 */
package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.log.LogbackConfigurator;

/**
 * 用于打印AutoConfigurationReport到单独的文件
 *
 * @author qiubo
 */
public class AutoConfigurationReportLogInitializer extends AbstractLogInitializer {
    @Override
    public void init(LogbackConfigurator configurator) {
        String fileName = "autoconfig-10dt.log";
        configurator.log("设置AUTO-CONFIG日志，日志文件为:%s", fileName);
        Appender<ILoggingEvent> appender =
                configurator.fileAppender(
                        "AUTO-CONFIG", "%d{yyyy-MM-dd HH:mm:ss.SSS} - %msg%n", fileName, 1);
        configurator.logger("AutoConfigurationReportLoggingInitializer", Level.DEBUG, false, appender);
    }
}
