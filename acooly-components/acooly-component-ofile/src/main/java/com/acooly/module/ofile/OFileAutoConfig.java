/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.module.ofile;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.acooly.module.ofile.OFileProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ OFileProperties.class })
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.ofile")
public class OFileAutoConfig {
    @Bean
    public AbstractDatabaseScriptIniter ofileScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM sys_ofile";
            }

            @Override
            public String getInitSqlFile(DatabaseType databaseType) {
                return "META-INF/database/mysql/ofile.sql";
            }
        };
    }
}
