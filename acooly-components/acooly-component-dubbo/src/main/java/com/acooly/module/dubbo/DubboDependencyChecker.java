/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-05-17 19:50 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.boot.component.DependencyChecker;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Ports;
import org.springframework.core.env.Environment;

/** @author qiubo@yiji.com */
public class DubboDependencyChecker implements DependencyChecker {
  @Override
  public void check(Environment environment) {
    if (environment.getProperty("acooly.dubbo.enable", Boolean.class, Boolean.TRUE)) {
      if (environment.getProperty("acooly.dubbo.provider.enable", Boolean.class, Boolean.TRUE)) {
        Integer port = environment.getRequiredProperty("acooly.dubbo.provider.port", Integer.class);
        if (Ports.isPortUsing(port.intValue())) {
          throw new AppConfigException("dubbo port:" + port + " is using.");
        }
      }
    }
  }
}
