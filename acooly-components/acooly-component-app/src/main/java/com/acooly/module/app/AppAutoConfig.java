/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.module.app;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static com.acooly.module.ofile.OFileProperties.PREFIX;

/**
 * @author kuli@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ AppProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.app")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class AppAutoConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private AppProperties appProperties;
	
	@Bean
	public AbstractDatabaseScriptIniter appScriptIniter() {
		return new AbstractDatabaseScriptIniter() {
			@Override
			public String getEvaluateSql(DatabaseType databaseType) {
				return "SELECT count(*) FROM app_version";
			}
			
			@Override
			public List<String> getInitSqlFile(DatabaseType databaseType) {
				return Lists.newArrayList("META-INF/database/mysql/app.sql");
			}
		};
	}
	
}
