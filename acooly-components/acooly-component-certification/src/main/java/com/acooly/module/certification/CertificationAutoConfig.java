/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-24 21:58 创建
 */
package com.acooly.module.certification;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.module.certification.CertificationProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ CertificationProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.certification")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class CertificationAutoConfig {
	@Bean
	public StandardDatabaseScriptIniter smsScriptIniter() {
		return new StandardDatabaseScriptIniter() {
			@Override
			public String getEvaluateTable() {
				return "certification_info";
			}
			
			@Override
			public String getComponentName() {
				return "certification";
			}
			
			@Override
			public List<String> getInitSqlFile() {
				return Lists.newArrayList("certification", "certification_urls");
			}
		};
	}
}
