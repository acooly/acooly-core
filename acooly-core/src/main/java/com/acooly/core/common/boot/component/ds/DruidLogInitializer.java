/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-15 12:32 创建
 *
 */
package com.acooly.core.common.boot.component.ds;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.log.LogbackAsyncAppender;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;

import static com.acooly.core.common.boot.component.ds.DruidProperties.ENABLE_KEY;

/**
 * 打印慢sql和big result sql到sql-10dt.log文件
 * @author qiubo
 */
public class DruidLogInitializer extends AbstractLogInitializer {
	@Override
	public void init(LogbackConfigurator configurator) {
		//此组件启用时才添加日志配置
		if (configurator.getEnvironment().getProperty(ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
			String fileName = "sql-2dt.log";
			configurator.log("设置数据库访问性能日志，日志文件为:%s", fileName);
			//创建异步file appender
			Appender<ILoggingEvent> appender = configurator.asyncFileAppender("DRUID-SQL", "%msg%n", fileName, 2);
			//异步日志不收集栈信息
			if (appender instanceof LogbackAsyncAppender) {
				((LogbackAsyncAppender) appender).setIncludeCallerData(false);
			}
			configurator.logger("com.yiji.common.ds.sql", Level.INFO, false, appender);
		}
	}
}
