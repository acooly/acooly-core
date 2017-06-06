package com.acooly.module.openapi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;

/** @author qiubo@yiji.com */
public class OpenAPIPerfLogInitializer extends AbstractLogInitializer {
  @Override
  public void init(LogbackConfigurator configurator) {
    String fileName = "api-pref.log";
    configurator.log("设置api性能日志，日志文件为:%s", fileName);
    Appender<ILoggingEvent> appender = configurator.fileAppender("api-perf", "%msg%n", fileName, 5);
    configurator.logger(
        "com.yiji.framework.openapi_PERFORMANCE_LOGGER", Level.INFO, false, appender);

    if (Apps.buildProperties(OpenAPIProperties.class).getQueryLogSeparationEnable()) {
      String apiQuery = "api-query-30de.log";
      configurator.log("设置info级别的api-query日志，日志文件为:%s", apiQuery);
      Appender<ILoggingEvent> apiQueryAppender =
          configurator.asyncFileAppender("api-query", configurator.getPattern(), apiQuery);
      configurator.logger("API-QUERY", Level.INFO, false, apiQueryAppender);
    }
  }
}
