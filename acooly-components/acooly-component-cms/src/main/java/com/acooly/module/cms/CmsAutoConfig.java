/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-18 00:56 创建
 */
package com.acooly.module.cms;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.cms.service.CmsContentSavedService;
import com.acooly.module.cms.service.impl.CmsContentSavedImpl;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author acooly
 */
@Configuration
@ConditionalOnProperty(value = "acooly.cms.enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.cms")
@AutoConfigureAfter(SecurityAutoConfig.class)
public class CmsAutoConfig extends WebMvcConfigurerAdapter {

    @Bean
    public StandardDatabaseScriptIniter cmsScriptIniter() {
        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "cms_content";
            }

            @Override
            public String getComponentName() {
                return "cms";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("cms", "cms_urls");
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(CmsContentSavedService.class)
    public CmsContentSavedService cmsContentSavedService() {
        return new CmsContentSavedImpl();
    }

}
