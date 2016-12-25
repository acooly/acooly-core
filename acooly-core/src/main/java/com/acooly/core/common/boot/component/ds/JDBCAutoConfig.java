/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 14:32 创建
 *
 */
package com.acooly.core.common.boot.component.ds;

import com.acooly.core.common.dao.jdbc.PagedJdbcTemplate;
import com.acooly.core.common.exception.AppConfigException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

import static com.acooly.core.common.boot.component.ds.DruidProperties.ENABLE_KEY;

/**
 * @author qiubo
 */
@Configuration
@EnableConfigurationProperties({ DruidProperties.class })
@ConditionalOnProperty(value = ENABLE_KEY, matchIfMissing = true)
@Slf4j
public class JDBCAutoConfig {
	
	@Autowired
	private DruidProperties druidProperties;
	
	@Bean
	public DataSource dataSource() {
		try {
			if (druidProperties == null) {
				return DruidProperties.buildFromEnv(DruidProperties.PREFIX);
			} else {
				return druidProperties.build();
			}
		} catch (Exception e) {
			//这种方式有点挫，先就这样吧
			log.error("初始化数据库连接池异常，关闭应用", e);
			System.exit(0);
			throw new AppConfigException("数据源配置异常", e);
		}
	}
	
	@Bean
	@ConditionalOnMissingBean(JdbcOperations.class)
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public PagedJdbcTemplate pagedJdbcTemplate(DataSource dataSource) {
		return new PagedJdbcTemplate(dataSource);
	}
	
	@Bean
	@ConditionalOnMissingBean(NamedParameterJdbcOperations.class)
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean
	@ConditionalOnMissingBean(TransactionTemplate.class)
	public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
		return new TransactionTemplate(platformTransactionManager);
	}
}
