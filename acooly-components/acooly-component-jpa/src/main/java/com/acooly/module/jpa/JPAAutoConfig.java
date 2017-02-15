/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:46 创建
 */
package com.acooly.module.jpa;

import com.acooly.core.common.boot.Apps;
import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.google.common.collect.Lists;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.persistence.EntityManagerFactory;
import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author qiubo
 */
@Configuration
@ConditionalOnProperty(value = JPAProperties.ENABLE_KEY, matchIfMissing = true)
@Import(JPAAutoConfig.JpaRegistrar.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class JPAAutoConfig {

	@Bean
	@ConditionalOnProperty(value = "acooly.jpa.openEntityManagerInViewFilterEnable", matchIfMissing = true)
	@ConditionalOnWebApplication
	public FilterRegistrationBean openEntityManagerInViewFilter() {
		OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp","/services/*").toArray(new String[0]));
		registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
		registration.setName("openSessionInViewFilter");
		registration.setOrder(Ordered.LOWEST_PRECEDENCE - 100);

		return registration;
	}
	
	@Bean
	public SessionFactory sessionFactory(EntityManagerFactory factory) {
		return factory.unwrap(SessionFactory.class);
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(	EntityManagerFactoryBuilder factoryBuilder,
																		DataSource dataSource,
																		JpaProperties properties) {
		Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
		vendorProperties.putAll(properties.getHibernateProperties(dataSource));
		return factoryBuilder.dataSource(dataSource)
			.packages(Lists.newArrayList(Apps.getBasePackage(), "com.acooly.module").toArray(new String[0]))
			.properties(vendorProperties).jta(false).build();
	}

	
	static class JpaRegistrar extends AbstractRepositoryConfigurationSourceSupport {
		
		@Override
		protected Class<? extends Annotation> getAnnotation() {
			return EnableJpaRepositories.class;
		}
		
		@Override
		protected Class<?> getConfiguration() {
			return EnableJpaRepositoriesConfiguration.class;
		}
		
		@Override
		protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
			return new JpaRepositoryConfigExtension();
		}
		
		@EnableJpaRepositories(repositoryBaseClass = AbstractEntityJpaDao.class)
		private class EnableJpaRepositoriesConfiguration {
		}
	}
	
}
