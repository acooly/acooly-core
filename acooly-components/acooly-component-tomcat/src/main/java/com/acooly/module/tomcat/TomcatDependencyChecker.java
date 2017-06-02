/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 15:29 创建
 */
package com.acooly.module.tomcat;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.DependencyChecker;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Ports;
import org.springframework.core.env.Environment;

/** @author qiubo@yiji.com */
public class TomcatDependencyChecker implements DependencyChecker {
  @Override
  public void check(Environment environment) {
    if (Ports.isPortUsing(Apps.getHttpPort())) {
      throw new AppConfigException("tomcat http port:" + Apps.getHttpPort() + " is using.");
    }
  }
}
