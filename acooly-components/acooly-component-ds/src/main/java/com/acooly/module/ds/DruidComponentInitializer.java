/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-12-09 10:31 创建
 *
 */
package com.acooly.module.ds;

import com.acooly.core.common.boot.component.ComponentInitializer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author qiubo
 */
public class DruidComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		//tuning druid logger ,avoid log4j initialize
		System.setProperty("druid.logType", "slf4j");
		//关闭org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer加载数据源,在加载多个数据源时会出错
		if (Strings.isNullOrEmpty(System.getProperty("spring.datasource.initialize"))) {
			System.setProperty("spring.datasource.initialize", Boolean.FALSE.toString());
		}
	}
	
	@Override
	public List<String> excludeAutoconfigClassNames() {
		return Lists
			.newArrayList("org.springframework.boot.devtools.autoconfigure.DevToolsDataSourceAutoConfiguration");
	}
}
