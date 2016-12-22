/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:46 创建
 */
package com.acooly.core.common.boot.component.jpa;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.dao.jpa.AbstractEntityJpaDao;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.acooly.core.common.boot.component.jpa.JpaConfig.JPADruidProperties.ENABLE_KEY;
import static com.acooly.core.common.boot.component.jpa.JpaConfig.JPADruidProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnProperty(value = ENABLE_KEY, matchIfMissing = true)
@Import(JpaConfig.JpaRegistrar.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class JpaConfig// extends HibernateJpaAutoConfiguration
{

//	public JpaConfig(DataSource dataSource, JpaProperties jpaProperties, ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
//		super(dataSource, jpaProperties, jtaTransactionManagerProvider);
//	}
//
//	@Override
//	protected String[] getPackagesToScan() {
//		return Lists.newArrayList(Apps.getBasePackage(),"com.acooly.module.security").toArray(new String[0]);
//	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder factoryBuilder,DataSource dataSource, JpaProperties properties) {
		Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
		vendorProperties.putAll(properties.getHibernateProperties(dataSource));
		return factoryBuilder.dataSource(dataSource).packages(Lists.newArrayList(Apps.getBasePackage(),"com.acooly.module.security").toArray(new String[0]))
				.properties(vendorProperties).jta(false).build();
	}
	@ConfigurationProperties(prefix = PREFIX)
	@Getter
	@Setter
	public static class JPADruidProperties {
		public static final String PREFIX = "acooly.jpa";
		public static final String ENABLE_KEY = PREFIX + ".enable";
		private boolean enable = true;
		
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
