/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:50 创建
 */
package com.acooly.module.dubbo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/** @author qiubo@yiji.com */
public class DubboShutdownApplicationListener implements ApplicationListener<ContextClosedEvent> {
  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    new DubboShutdownHook().run();
  }
}
