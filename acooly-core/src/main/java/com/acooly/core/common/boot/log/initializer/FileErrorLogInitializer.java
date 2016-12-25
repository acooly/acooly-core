/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-14 15:03 创建
 *
 */
package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.log.LogbackConfigurator;

/**
 * 添加error文件日志
 * @author qiubo
 */
public class FileErrorLogInitializer extends AbstractLogInitializer {
	@Override
	public void init(LogbackConfigurator configurator) {
		String fileName = Apps.getAppName() + "-error-30de.log";
		configurator.log("设置error级别的文件日志，日志文件为:%s", fileName);
		Appender<ILoggingEvent> appender = configurator.asyncFileAppender("FILE-ERROR", configurator.getPattern(),
			fileName);
		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setLevel("ERROR");
		configurator.start(thresholdFilter);
		appender.addFilter(thresholdFilter);
		configurator.getRootLogger().addAppender(appender);
	}
}
