/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-02-14 17:04 创建
 */
package com.acooly.module.store;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author zhangpu@acooly.cn
 */
@Configuration
@EnableConfigurationProperties({StoreProperties.class})
@ConditionalOnProperty(value = StoreProperties.PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.store")
public class StoreAutoConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private StoreProperties storeProperties;

    /**
     * 数据库初始化
     *
     * @return
     */
    @Bean
    public StandardDatabaseScriptIniter PortletScriptIniter() {

        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "st_store";
            }

            @Override
            public String getComponentName() {
                return "store";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("store", "store_urls");
            }
        };
    }


}
