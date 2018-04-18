/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.module.openapi;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.acooly.openapi.framework.core.servlet.OpenAPIDispatchServlet;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

import static com.acooly.module.openapi.OpenAPIProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({OpenAPIProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.framework")
@EnableJpaRepositories(
        repositoryBaseClass = AbstractEntityJpaDao.class,
        basePackages = "com.acooly.openapi.framework"
)
@EntityScan(basePackages = "com.acooly.openapi.framework.domain")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class OpenAPIAutoConfig {
    @Autowired
    private OpenAPIProperties properties;

    @Bean
    public ServletRegistrationBean openAPIServlet(OpenAPIProperties properties) {
        ServletRegistrationBean bean = new ServletRegistrationBean();
        bean.setUrlMappings(Lists.newArrayList("/gateway.html", "/gateway", "/gateway.do"));
        OpenAPIDispatchServlet openAPIDispatchServlet = new OpenAPIDispatchServlet();
        bean.setServlet(openAPIDispatchServlet);
        return bean;
    }

    @Bean
    public AbstractDatabaseScriptIniter openapiCoreScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM api_order_info";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                if (databaseType == DatabaseType.mysql) {
                    return Lists.newArrayList("META-INF/database/mysql/openapi-core.sql");
                } else {
                    return Lists.newArrayList("META-INF/database/oracle/openapi-core.sql");
                }
            }
        };
    }

    @Bean
    public AbstractDatabaseScriptIniter openapiManageScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM api_partner";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                if (databaseType == DatabaseType.mysql) {
                    return Lists.newArrayList(
                            "META-INF/database/mysql/openapi-manage.sql",
                            "META-INF/database/mysql/openapi-initTest.sql",
                            "META-INF/database/mysql/openapi-manage-urls.sql");
                } else {
                    throw new UnsupportedOperationException("还不支持oracle");
                }
            }
        };
    }

    @Configuration
    @ConditionalOnProperty("dubbo.provider.enable")
    @ImportResource("classpath:spring/openapi/openapi-facade-dubbo-provider.xml")
    public static class OpenApiRemoteServiceConfig {

    }
}
