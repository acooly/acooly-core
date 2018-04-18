/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-04-15 16:00 创建
 */
package com.acooly.core.common.boot.log;

import com.acooly.core.common.boot.Apps;
import com.google.common.base.Strings;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

/**
 * @author qiubo
 */
public class AnsiSupportApplicationListener
        implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if (IS_OS_WINDOWS || (IS_OS_LINUX && !Apps.isDevMode())) {
            return;
        }
        if (Strings.isNullOrEmpty(System.getProperty("spring.output.ansi.enabled"))) {
            System.setProperty("spring.output.ansi.enabled", "ALWAYS");
        }
    }
}
