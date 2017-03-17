/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.module.portlet;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnProperty(value = "acooly.portlet.enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.portlet")
public class PortletAutoConfig extends WebMvcConfigurerAdapter {

    @Bean
    public StandardDatabaseScriptIniter PortletScriptIniter() {

        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "portlet_feedback";
            }

            @Override
            public String getComponentName() {
                return "portlet";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("portlet", "portlet_urls");
            }

        };
    }

}
