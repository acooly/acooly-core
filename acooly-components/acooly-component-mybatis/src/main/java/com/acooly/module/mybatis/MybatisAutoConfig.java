/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 10:42 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.JdbcAutoConfig;
import com.acooly.module.mybatis.interceptor.DateInterceptor;
import com.acooly.module.mybatis.interceptor.ExInterceptor;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

import static com.acooly.module.mybatis.MybatisProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@Import({MapperScannerRegistrar.class})
@EnableConfigurationProperties({MybatisProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@AutoConfigureAfter(JdbcAutoConfig.class)
public class MybatisAutoConfig  {


    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource,
                                               MybatisProperties properties) {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();

        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
            factory.setConfigLocation(
                    this.resourceLoader.getResource(properties.getConfigLocation()));
        }
        Map<String, Interceptor> beansOfType = Apps.getApplicationContext().getBeansOfType(Interceptor.class);
        if (!ObjectUtils.isEmpty(beansOfType)) {
            Interceptor[] interceptors = beansOfType.values().toArray(new Interceptor[0]);
            AnnotationAwareOrderComparator.sort(interceptors);
            factory.setPlugins(interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (!properties.getTypeAliasesPackage().isEmpty()) {
            String packages = Joiner.on(',')
                    .join(properties.getTypeAliasesPackage().values().iterator());
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
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
            Interceptor interceptor = beansOfType.get("pageExecutorInterceptor");
            if (null != interceptor) {
                ((PageExecutorInterceptor) interceptor).setSqlSessionFactory(sqlSessionFactory);
            }
        } catch (Exception e) {
            throw new AppConfigException(e);
        }
        customConfig(sqlSessionFactory.getConfiguration(), properties);
        return sqlSessionFactory;
    }

    private void customConfig(
            org.apache.ibatis.session.Configuration configuration,
            MybatisProperties mybatisProperties) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(configuration);
        mybatisProperties
                .getSettings()
                .entrySet()
                .stream()
                .forEach(entry -> wrapper.setPropertyValue(entry.getKey(), entry.getValue()));
        configuration.setObjectFactory(new PageObjectFactory());
        configuration.setObjectWrapperFactory(new PageObjectWrapperFactory());
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
    public PageExecutorInterceptor pageExecutorInterceptor() {
        return new PageExecutorInterceptor();
    }

    @Bean
    public DateInterceptor updateInterceptor() {
        return new DateInterceptor();
    }

    @Bean
    public ExInterceptor exInterceptor() {
        return new ExInterceptor();
    }


}
