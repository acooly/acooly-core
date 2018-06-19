package com.acooly.module.config;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author qiubo
 */
@Configuration
@ComponentScan(basePackages = "com.acooly.module.config")
public class AppConfigAutoConfig {

    @Bean
    public StandardDatabaseScriptIniter appConfigScriptIniter() {
        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "sys_app_config";
            }

            @Override
            public String getComponentName() {
                return "config";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("config", "config_urls");
            }
        };
    }
}
