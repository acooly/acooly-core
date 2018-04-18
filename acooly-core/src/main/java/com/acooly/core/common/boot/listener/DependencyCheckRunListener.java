/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:43 创建
 */
package com.acooly.core.common.boot.listener;

import com.acooly.core.common.boot.component.DependencyChecker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public class DependencyCheckRunListener implements SpringApplicationRunListener {
    private SpringApplication application;
    private String[] args;

    public DependencyCheckRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {
        // Do nothing
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
        List<DependencyChecker> dependencyCheckers =
                SpringFactoriesLoader.loadFactories(
                        DependencyChecker.class, ClassUtils.getDefaultClassLoader());
        dependencyCheckers
                .stream()
                .forEach(
                        dependencyChecker -> {
                            dependencyChecker.check(configurableApplicationContext.getEnvironment());
                        });
    }

    @Override
    public void finished(
            ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
    }
}
