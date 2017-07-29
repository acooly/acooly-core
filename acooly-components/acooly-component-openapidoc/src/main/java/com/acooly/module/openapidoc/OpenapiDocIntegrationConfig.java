/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-14 01:21 创建
 */
package com.acooly.module.openapidoc;

import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.acooly.module.security.config.SecurityAutoConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** @author qiubo@yiji.com */
@Configuration
@ConditionalOnProperty(value = "acooly.openapi.apidoc.enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.apidoc")
@EnableJpaRepositories(
  repositoryBaseClass = AbstractEntityJpaDao.class,
  basePackages = "com.acooly.openapi.apidoc"
)
@AutoConfigureAfter(SecurityAutoConfig.class)
public class OpenapiDocIntegrationConfig {}
