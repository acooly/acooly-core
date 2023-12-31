/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:46 创建
 */
package com.acooly.module.jpa;

import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.acooly.module.jpa.ex.DatabaseLookup;
import com.acooly.module.jpa.ex.ExHibernateJpaVendorAdapter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Map;

/**
 * @author qiubo
 * @author zhangpu for 5.x
 */
@Configuration
@ConditionalOnProperty(value = JPAProperties.ENABLE_KEY, matchIfMissing = true)
@Import(JPAAutoConfig.JpaRegistrar.class)
@AutoConfigureBefore(HibernateJpaAutoConfiguration.class)
@EnableConfigurationProperties({JPAProperties.class, HibernateProperties.class})
@EnableJpaRepositories(repositoryBaseClass = AbstractEntityJpaDao.class, basePackages = "com.acooly.module.**.dao")
public class JPAAutoConfig {


    @Bean
    @ConditionalOnProperty(value = "acooly.jpa.openEntityManagerInViewFilterEnable", matchIfMissing = true
    )
    @ConditionalOnWebApplication
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter(JPAProperties properties) {
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
        FilterRegistrationBean<OpenEntityManagerInViewFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns(
                properties.getOpenEntityManagerInViewFilterUrlPatterns().toArray(new String[0]));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        registration.setName("openSessionInViewFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE - 100);

        return registration;
    }

    /**
     * 扩展支持自动生成表
     *
     * @param properties
     * @param dataSource
     * @return
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties properties, DataSource dataSource) {
        AbstractJpaVendorAdapter adapter = new ExHibernateJpaVendorAdapter();
        adapter.setShowSql(properties.isShowSql());
        adapter.setDatabase(DatabaseLookup.getDatabase(dataSource));
        adapter.setDatabasePlatform(properties.getDatabasePlatform());
        adapter.setGenerateDdl(properties.isGenerateDdl());
        return adapter;
    }

    /**
     * 自定义 EntityManagerFactory
     * <ORDER_PATTERN>
     * 目的：主要为扩展自定义默认的entity扫码路径
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder, DataSource dataSource,
                                                                       JpaProperties properties, HibernateProperties hibernateProperties, JPAProperties jpaProperties) {
        Map<String, Object> vendorProperties = hibernateProperties.
                determineHibernateProperties(properties.getProperties(), new HibernateSettings());
        return factoryBuilder.dataSource(dataSource)
                .packages(jpaProperties.getEntityPackagesToScan().values().toArray(new String[0]))
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

        @EnableJpaRepositories(
                repositoryBaseClass = AbstractEntityJpaDao.class,
                basePackages = "com.acooly.module.**.dao"
        )
        private class EnableJpaRepositoriesConfiguration {
        }
    }
}
