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
import org.slf4j.Logger;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * 解析yiji.log.level配置
 *
 * <p>用户可以通过配置yiji.log.level.com.yiji=debug 设置com.yiji的日志级别
 *
 * @author qiubo
 */
@Order
public class LevelLogInitializer extends AbstractLogInitializer {
  @Override
  public void init(LogbackConfigurator configurator) {
    Map<String, Object> levels =
        new RelaxedPropertyResolver(configurator.getEnvironment())
            .getSubProperties("acooly.log.level.");
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
