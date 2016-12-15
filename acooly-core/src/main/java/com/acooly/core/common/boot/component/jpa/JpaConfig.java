/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:46 创建
 */
package com.acooly.core.common.boot.component.jpa;

import com.acooly.core.common.dao.jpa.AbstractEntityJpaDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

import static com.acooly.core.common.boot.component.jpa.JpaConfig.JPADruidProperties.ENABLE_KEY;
import static com.acooly.core.common.boot.component.jpa.JpaConfig.JPADruidProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnProperty(value = ENABLE_KEY, matchIfMissing = true)
@Import(JpaConfig.JpaRegistrar.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class JpaConfig {
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
