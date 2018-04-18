/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-22 19:28 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.module.ds.DruidProperties;
import com.acooly.module.ds.PagedJdbcTemplate;
import com.acooly.module.mybatis.interceptor.DateInterceptor;
import com.acooly.module.mybatis.interceptor.PageExecutorInterceptor;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

import static com.acooly.module.mybatis.MybatisProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnProperty(value = PREFIX + ".supportMultiDataSource")
@Slf4j
@Import({MapperScannerRegistrar.class, MultiMybatisAutoConfig.Initializer.class})
public class MultiMybatisAutoConfig {

    private static MybatisAutoConfig createMybatisAutoConfig() {
        ConfigurableListableBeanFactory factory =
                ((AbstractApplicationContext) ApplicationContextHolder.get()).getBeanFactory();
        MybatisAutoConfig mybatisAutoConfig = new MybatisAutoConfig();
        DatabaseIdProvider databaseIdProvider = null;
        try {
            databaseIdProvider = factory.getBean(DatabaseIdProvider.class);
        } catch (NoSuchBeanDefinitionException e) {
            //ignore
        }
        ResourceLoader resourceLoader = null;
        try {
            resourceLoader = factory.getBean(ResourceLoader.class);
        } catch (BeansException e) {
            resourceLoader = new DefaultResourceLoader();
        }
        Interceptor[] interceptors =
                factory.getBeansOfType(Interceptor.class).values().toArray(new Interceptor[0]);
        mybatisAutoConfig.setDatabaseIdProvider(databaseIdProvider);
        mybatisAutoConfig.setInterceptors(interceptors);
        mybatisAutoConfig.setResourceLoader(resourceLoader);
        return mybatisAutoConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public PageExecutorInterceptor pageExecutorInterceptor() {
        return new PageExecutorInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DateInterceptor updateInterceptor() {
        return new DateInterceptor();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource(MybatisProperties mybatisProperties) {
        for (Map.Entry<String, MybatisProperties.Multi> entry :
                mybatisProperties.getMulti().entrySet()) {
            if (entry.getValue().isPrimary()) {
                return DruidProperties.buildFromEnv(entry.getValue().getDsPrefix());
            }
        }
        throw null;
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PagedJdbcTemplate pagedJdbcTemplate(DataSource dataSource) {
        return new PagedJdbcTemplate(dataSource);
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            MybatisProperties mybatisProperties, @Qualifier("dataSource") DataSource dataSource)
            throws Exception {

        return createMybatisAutoConfig().sqlSessionFactory(dataSource, mybatisProperties);
    }

    static class Initializer implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(
                AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            ConfigurableListableBeanFactory factory =
                    ((AbstractApplicationContext) ApplicationContextHolder.get()).getBeanFactory();
            MybatisProperties mybatisProperties = Apps.buildProperties(MybatisProperties.class);
            if (mybatisProperties.isSupportMultiDataSource()) {
                for (Map.Entry<String, MybatisProperties.Multi> entry :
                        mybatisProperties.getMulti().entrySet()) {
                    if (!entry.getValue().isPrimary()) {
                        DruidDataSource druidDataSource =
                                DruidProperties.buildFromEnv(entry.getValue().getDsPrefix());
                        String dataSourceName = entry.getKey() + "DataSource";
                        factory.registerSingleton(dataSourceName, druidDataSource);
                        SqlSessionFactory sqlSessionFactory =
                                createMybatisAutoConfig().sqlSessionFactory(druidDataSource, mybatisProperties);
                        String sqlSessionFactoryName = entry.getKey() + "SqlSessionFactory";
                        factory.registerSingleton(sqlSessionFactoryName, sqlSessionFactory);
                        String jdbcTemplateName = entry.getKey() + "JdbcTemplate";
                        factory.registerSingleton(jdbcTemplateName, new JdbcTemplate(druidDataSource));
                        String pagedJdbcTemplateName = entry.getKey() + "PagedJdbcTemplate";
                        factory.registerSingleton(
                                pagedJdbcTemplateName, new PagedJdbcTemplate(druidDataSource));
                    }
                }
            }
        }
    }
}
