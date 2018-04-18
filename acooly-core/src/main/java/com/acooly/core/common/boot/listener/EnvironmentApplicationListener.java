/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 15:02 创建
 */
package com.acooly.core.common.boot.listener;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.google.common.base.Strings;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author qiubo@yiji.com
 */
public class EnvironmentApplicationListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, PriorityOrdered {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        new EnvironmentHolder().setEnvironment(event.getEnvironment());
        setProfileIfEnableActiveProfiles(event.getEnvironment());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private void setProfileIfEnableActiveProfiles(ConfigurableEnvironment environment) {
        if (Strings.isNullOrEmpty(System.getProperty(Apps.SPRING_PROFILE_ACTIVE))) {
            String profile = environment.getProperty(Apps.SPRING_PROFILE_ACTIVE);
            if (!Strings.isNullOrEmpty(profile)) {
                System.setProperty(Apps.SPRING_PROFILE_ACTIVE, profile);
            }
        }
    }
}
