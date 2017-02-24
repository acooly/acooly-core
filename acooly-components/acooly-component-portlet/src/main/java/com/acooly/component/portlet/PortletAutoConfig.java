/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.component.portlet;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.acooly.module.mybatis.MybatisAutoConfig;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnProperty(value = "acooly.portlet.enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.component.portlet")
public class PortletAutoConfig extends WebMvcConfigurerAdapter {

    @Bean
    public AbstractDatabaseScriptIniter PortletScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM p_feedback";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                return Lists.newArrayList("META-INF/database/mysql/portlet.sql");
            }
        };
    }

}
