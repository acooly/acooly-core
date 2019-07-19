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
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiubo
 */
@Configuration
@ConditionalOnProperty(value = JPAProperties.ENABLE_KEY, matchIfMissing = true)
@Import(JPAAutoConfig.JpaRegistrar.class)
@AutoConfigureBefore(HibernateJpaAutoConfiguration.class)
@EnableConfigurationProperties({JPAProperties.class})
@EnableJpaRepositories(
        repositoryBaseClass = AbstractEntityJpaDao.class,
        basePackages = "com.acooly.module.**.dao"
)
public class JPAAutoConfig {

    @Bean
    @ConditionalOnProperty(
            value = "acooly.jpa.openEntityManagerInViewFilterEnable",
            matchIfMissing = true
    )
    @ConditionalOnWebApplication
    public FilterRegistrationBean openEntityManagerInViewFilter(JPAProperties properties) {
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(
                properties.getOpenEntityManagerInViewFilterUrlPatterns().toArray(new String[0]));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        registration.setName("openSessionInViewFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE - 100);

        return registration;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties properties, DataSource dataSource) {
        AbstractJpaVendorAdapter adapter = new ExHibernateJpaVendorAdapter();
        adapter.setShowSql(properties.isShowSql());
        adapter.setDatabase(properties.determineDatabase(dataSource));
        adapter.setDatabasePlatform(properties.getDatabasePlatform());
        adapter.setGenerateDdl(properties.isGenerateDdl());
        return adapter;
    }

//    @Bean
//    public SessionFactory sessionFactory(EntityManagerFactory factory) {
//        return factory.unwrap(SessionFactory.class);
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder factoryBuilder,
            DataSource dataSource,
            JpaProperties properties,
            JPAProperties jpaProperties) {
        Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
        vendorProperties.putAll(properties.getProperties());
        return factoryBuilder
                .dataSource(dataSource)
                .packages(jpaProperties.getEntityPackagesToScan().values().toArray(new String[0]))
                .properties(vendorProperties)
                .jta(false)
                .build();
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
