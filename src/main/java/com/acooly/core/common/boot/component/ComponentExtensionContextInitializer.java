/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:07 创建
 */
package com.acooly.core.common.boot.component;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class ComponentExtensionContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(applicationContext.getEnvironment(),
                "spring.autoconfigure.");
        String[] exclude = resolver.getProperty("exclude", String[].class);
        if (exclude == null) {
            exclude = new String[0];
        }
        List<String> excludes = Lists.newArrayList(exclude);
        SpringFactoriesLoader.loadFactories(ComponentInitializer.class,
                applicationContext.getClassLoader()).forEach(componentInitializer -> {
            componentInitializer.initialize(applicationContext);
            excludes.addAll(componentInitializer.excludeAutoconfigClassNames());
        });
        if (!excludes.isEmpty()) {
            System.setProperty("spring.autoconfigure.exclude", Joiner.on(',').join(excludes));
        }
    }
}
