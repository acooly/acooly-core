/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 14:32 创建
 *
 */
package com.acooly.module.ds;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.dao.support.DataSourceReadyEvent;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.check.DBPatchChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author qiubo
 */
@Configuration
@EnableConfigurationProperties({DruidProperties.class})
@ConditionalOnProperty(value = DruidProperties.ENABLE_KEY, matchIfMissing = true)
@Slf4j
public class JdbcAutoConfig {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        DataSource dataSource;
        try {
            if (druidProperties == null) {
                druidProperties = new DruidProperties();
                EnvironmentHolder.buildProperties(druidProperties, DruidProperties.PREFIX);
            }
            dataSource = druidProperties.build();
//            if (druidProperties.isUseTomcatDataSource()) {
//                dataSource = new TomcatDataSourceProperties().build(druidProperties);
//            }
            if (druidProperties.isAutoCreateTable()) {
                ApplicationContextHolder.get().publishEvent(new DataSourceReadyEvent(dataSource));
            }
            new DBPatchChecker(druidProperties).check(dataSource);
            return dataSource;
        } catch (Exception e) {
            //这种方式有点挫，先就这样吧
            log.error("初始化数据库连接池异常，关闭应用", e);
            System.exit(0);
            throw new AppConfigException("数据源配置异常", e);
        }
    }

    @Bean
    @ConditionalOnMissingBean(JdbcOperations.class)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(NamedParameterJdbcOperations.class)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionTemplate.class)
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }
}
