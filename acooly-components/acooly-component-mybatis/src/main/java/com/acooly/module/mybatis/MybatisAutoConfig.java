/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 10:42 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.component.ds.JDBCAutoConfig;
import com.acooly.module.mybatis.interceptor.PageExecutorInterceptor;
import com.acooly.module.mybatis.page.PageObjectFactory;
import com.acooly.module.mybatis.page.PageObjectWrapperFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import static com.acooly.module.mybatis.MybatisProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@Import({ MapperScannerRegistrar.class })
@EnableConfigurationProperties({ MybatisProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@AutoConfigureAfter(JDBCAutoConfig.class)
public class MybatisAutoConfig {
	
	@Autowired(required = false)
	private Interceptor[] interceptors;
	
	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;
	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisProperties properties) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(properties.getConfigLocation()));
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
			factory.setMapperLocations(properties.resolveMapperLocations());
		}
		SqlSessionFactory sqlSessionFactory = factory.getObject();
		customConfig(sqlSessionFactory.getConfiguration(), properties);
		return sqlSessionFactory;
	}
	
	private void customConfig(org.apache.ibatis.session.Configuration configuration,
								MybatisProperties mybatisProperties) {
		BeanWrapperImpl wrapper = new BeanWrapperImpl(configuration);
		mybatisProperties.getSettings().entrySet().stream()
			.forEach(entry -> wrapper.setPropertyValue(entry.getKey(), entry.getValue()));
		configuration.setObjectFactory(new PageObjectFactory());
		configuration.setObjectWrapperFactory(new PageObjectWrapperFactory());
	}
	
	@Bean
	public PageExecutorInterceptor pageExecutorInterceptor() {
		return new PageExecutorInterceptor();
	}

}
