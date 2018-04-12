package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.log.LogbackAsyncAppender;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.log.BusinessLog;

public class DataCollectorLogInitializer extends AbstractLogInitializer {
  @Override
  public void init(LogbackConfigurator configurator) {
    String fileName = "busi/business-2dt.log";
    configurator.log("设置业务监控日志，日志文件为:%s", fileName);
    // 创建异步file appender
    Appender<ILoggingEvent> appender =
        configurator.asyncFileAppender(BusinessLog.LOGGER_NAME, "%msg%n", fileName, 5);
    // 异步日志不收集栈信息
    if (appender instanceof LogbackAsyncAppender) {
      ((LogbackAsyncAppender) appender).setIncludeCallerData(false);
    }
    configurator.logger(BusinessLog.LOGGER_NAME, Level.INFO, false, appender);
  }
}
