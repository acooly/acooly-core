/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:07 创建
 */
package com.acooly.core.common.boot.component;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.listener.DevModeDetector;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

import static com.acooly.core.common.boot.listener.ExApplicationRunListener.COMPONENTS_PACKAGE;

/** @author qiubo */
@Order(value = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class ComponentExtensionContextInitializer implements ApplicationContextInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    new DevModeDetector().apply(applicationContext.getEnvironment());
    RelaxedPropertyResolver resolver =
        new RelaxedPropertyResolver(applicationContext.getEnvironment(), "spring.autoconfigure.");
    String[] exclude = resolver.getProperty("exclude", String[].class);
    if (exclude == null) {
      exclude = new String[0];
    }
    List<String> excludes = Lists.newArrayList(exclude);
    SpringFactoriesLoader.loadFactoryNames(
            EnableAutoConfiguration.class, applicationContext.getClassLoader())
        .forEach(
            autoConfiguration -> {
              if (autoConfiguration.startsWith("com.acooly.core")) {
                return;
              }
              if (autoConfiguration.startsWith("com.acooly")) {
                if (!autoConfiguration.startsWith(COMPONENTS_PACKAGE)) {
                  log.error("组件：{}没有遵循规范，必须放在{}子包", autoConfiguration, COMPONENTS_PACKAGE);
                  Apps.shutdown();
                }
              }
            });
    SpringFactoriesLoader.loadFactories(
            ComponentInitializer.class, applicationContext.getClassLoader())
        .forEach(
            componentInitializer -> {
              componentInitializer.initialize(applicationContext);
              excludes.addAll(componentInitializer.excludeAutoconfigClassNames());
            });

    if (!excludes.isEmpty()) {
      System.setProperty("spring.autoconfigure.exclude", Joiner.on(',').join(excludes));
    }
  }
}
