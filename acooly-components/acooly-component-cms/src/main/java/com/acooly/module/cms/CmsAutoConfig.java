/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-18 00:56 创建
 */
package com.acooly.module.cms;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author acooly
 */
@Configuration
@ConditionalOnProperty(value = "acooly.cms.enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.cms")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class CmsAutoConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public AbstractDatabaseScriptIniter cmsScriptIniter() {
		return new AbstractDatabaseScriptIniter() {
			@Override
			public String getEvaluateSql(DatabaseType databaseType) {
				return "SELECT count(*) FROM cms_content";
			}
			
			@Override
			public List<String> getInitSqlFile(DatabaseType databaseType) {
				return Lists.newArrayList("META-INF/database/mysql/cms.sql", "META-INF/database/mysql/cms_urls.sql");
			}
		};
	}
	
}
