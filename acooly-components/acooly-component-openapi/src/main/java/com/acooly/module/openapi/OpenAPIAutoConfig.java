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
import com.google.common.collect.Lists;
import com.yiji.framework.openapi.core.servlet.OpenAPIDispatchServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static com.acooly.module.openapi.OpenAPIProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ OpenAPIProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.yiji.framework.openapi.core")
@ComponentScan(basePackages = "com.yiji.framework.openapi.service")
@ComponentScan(basePackages = "com.yiji.framework.openapi.manage.web")
@EnableJpaRepositories(repositoryBaseClass = AbstractEntityJpaDao.class,
    basePackages = "com.yiji.framework.openapi.service.persistent")
public class OpenAPIAutoConfig {
	@Autowired
	private OpenAPIProperties properties;
	
	@Bean
	public ServletRegistrationBean openAPIServlet(OpenAPIProperties properties) {
		ServletRegistrationBean bean = new ServletRegistrationBean();
		bean.setUrlMappings(Lists.newArrayList("/gateway.html", "/gateway"));
		OpenAPIDispatchServlet openAPIDispatchServlet = new OpenAPIDispatchServlet();
		bean.setServlet(openAPIDispatchServlet);
		return bean;
	}
	
	@Bean
	public ThreadPoolTaskExecutor openApiTaskExecutor(OpenAPIProperties properties) {
		ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
		bean.setCorePoolSize(1);
		bean.setMaxPoolSize(20);
		bean.setQueueCapacity(100);
		bean.setKeepAliveSeconds(300);
		bean.setWaitForTasksToCompleteOnShutdown(true);
		bean.setAllowCoreThreadTimeOut(true);
		bean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
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
			public String getInitSqlFile(DatabaseType databaseType) {
				if (databaseType == DatabaseType.mysql) {
					return "META-INF/database/mysql/openapi-core.sql";
				} else {
					return "META-INF/database/oracle/openapi-core.sql";
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
            public String getInitSqlFile(DatabaseType databaseType) {
                if (databaseType == DatabaseType.mysql) {
                    return "META-INF/database/mysql/openapi-manage.sql";
                } else {
                    throw new UnsupportedOperationException("还不支持oracle");
                }
            }
        };
    }
}