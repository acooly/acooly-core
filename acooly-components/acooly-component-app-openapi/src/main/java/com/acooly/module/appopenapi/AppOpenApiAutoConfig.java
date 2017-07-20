/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 22:18 创建
 */
package com.acooly.module.appopenapi;

import com.acooly.module.appopenapi.support.AppApiLoginService;
import com.acooly.module.appopenapi.support.login.AnonymousAppApiLoginService;
import com.yiji.framework.openapi.core.notify.impl.DefaultApiNotifySender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.acooly.module.appopenapi.AppOpenapiProperties.PREFIX;

/** @author qiubo@yiji.com */
@Configuration
@EnableConfigurationProperties({AppOpenapiProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.appopenapi")
@Slf4j
public class AppOpenApiAutoConfig {

  @ConditionalOnMissingBean(AppApiLoginService.class)
  @Bean
  public AppApiLoginService appApiLoginService() {
    return new AnonymousAppApiLoginService();
  }

  @Bean
  public DefaultApiNotifySender defaultApiNotifySender(AppOpenapiProperties appOpenapiProperties) {
    DefaultApiNotifySender sender = new DefaultApiNotifySender();
    sender.setConnectionTimeout(appOpenapiProperties.getNotifySender().getConnectionTimeout());
    sender.setSocketTimeout(appOpenapiProperties.getNotifySender().getSocketTimeout());
    return sender;
  }
}
