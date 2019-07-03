/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-10-19 11:13 创建
 *
 */
package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.Level;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.support.PropertySourceUtils;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * 解析acooly.log.level配置
 *
 * <p>用户可以通过配置acooly.log.level.com.acooly=debug 设置com.acooly的日志级别
 *
 * @author qiubo
 * @author zhangpu 2019-06-19
 */
@Order
public class LevelLogInitializer extends AbstractLogInitializer {
    @Override
    public void init(LogbackConfigurator configurator) {
        Map<String, Object> levels = PropertySourceUtils.getSubProperties(configurator.getEnvironment(), "acooly.log.level.");
        for (Map.Entry<String, Object> entry : levels.entrySet()) {
            String loggerName = entry.getKey();
            String level = entry.getValue().toString();
            configurator.log("设置loggerName=%s,日志级别为:%s", loggerName, level);
            if (loggerName.equalsIgnoreCase("root")) {
                loggerName = Logger.ROOT_LOGGER_NAME;
            }
            configurator.logger(loggerName, Level.toLevel(level));
        }
    }
}
