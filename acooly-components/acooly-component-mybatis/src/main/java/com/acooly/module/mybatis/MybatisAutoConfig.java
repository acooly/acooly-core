/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 10:42 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.JDBCAutoConfig;
import com.acooly.module.mybatis.interceptor.DateInterceptor;
import com.acooly.module.mybatis.interceptor.PageExecutorInterceptor;
import com.acooly.module.mybatis.page.PageObjectFactory;
import com.acooly.module.mybatis.page.PageObjectWrapperFactory;
import com.google.common.base.Joiner;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import static com.acooly.module.mybatis.MybatisProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@Import({MapperScannerRegistrar.class})
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@AutoConfigureAfter(JDBCAutoConfig.class)
public class MybatisAutoConfig {

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisProperties properties) {
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
        if (!properties.getTypeAliasesPackage().isEmpty()) {
            String packages = Joiner.on(',').join(properties.getTypeAliasesPackage().values().iterator());
            factory.setTypeAliasesPackage(packages);
        }
        factory.setTypeAliasesSuperType(Entityable.class);
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }
        SqlSessionFactory sqlSessionFactory;
        try {
            sqlSessionFactory = factory.getObject();
        } catch (Exception e) {
            throw new AppConfigException(e);
        }
        customConfig(sqlSessionFactory.getConfiguration(), properties);
        return sqlSessionFactory;
    }

    private void customConfig(
            org.apache.ibatis.session.Configuration configuration, MybatisProperties mybatisProperties) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(configuration);
        mybatisProperties
                .getSettings()
                .entrySet()
                .stream()
                .forEach(entry -> wrapper.setPropertyValue(entry.getKey(), entry.getValue()));
        configuration.setObjectFactory(new PageObjectFactory());
        configuration.setObjectWrapperFactory(new PageObjectWrapperFactory());
    }

    public Interceptor[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Interceptor[] interceptors) {
        this.interceptors = interceptors;
    }

    public DatabaseIdProvider getDatabaseIdProvider() {
        return databaseIdProvider;
    }

    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @ConditionalOnMissingBean(PageExecutorInterceptor.class)
    public PageExecutorInterceptor pageExecutorInterceptor() {
        return new PageExecutorInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DateInterceptor updateInterceptor() {
        return new DateInterceptor();
    }
}
