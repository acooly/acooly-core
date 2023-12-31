/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-22 17:30 创建
 */
package com.acooly.module.ds;

import com.acooly.module.ds.check.DatabaseTableChecker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiubo@yiji.com
 */
@ConditionalOnProperty(value = "acooly.ds.checker.checkColumn")
@EnableConfigurationProperties({DruidProperties.class})
@Configuration
public class DatabaseTableCheckerConfig {
    @Bean
    public DatabaseTableChecker databaseTableChecker(DruidProperties druidProperties) {
        return new DatabaseTableChecker(druidProperties);
    }
}
