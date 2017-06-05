package com.acooly.module.openapi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;

/** @author qiubo@yiji.com */
public class OpenAPIPerfLogInitializer extends AbstractLogInitializer {
  @Override
  public void init(LogbackConfigurator configurator) {
    String fileName = "api-pref.log";
    configurator.log("设置api性能日志，日志文件为:%s", fileName);
    Appender<ILoggingEvent> appender =
        configurator.fileAppender("api-perf", "%msg%n", fileName, 5);
    configurator.logger("org.perf4j.TimingLogger", Level.INFO, false, appender);
  }
}
