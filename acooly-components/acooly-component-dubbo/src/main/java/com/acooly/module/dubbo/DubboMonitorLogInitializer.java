/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-21 17:01 创建
 *
 */
package com.acooly.module.dubbo;

import ch.qos.logback.classic.Level;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;

/**
 * 修改dubbo monitor的日志级别
 * @author qiubo@yiji.com
 */
public class DubboMonitorLogInitializer extends AbstractLogInitializer {
	@Override
	public void init(LogbackConfigurator configurator) {
		if (configurator.getEnvironment().getProperty("yiji.dubbo.enable", Boolean.class, Boolean.TRUE)) {
			configurator.logger("com.alibaba.dubbo.monitor.dubbo.DubboMonitor", Level.WARN);
		}
	}
}
