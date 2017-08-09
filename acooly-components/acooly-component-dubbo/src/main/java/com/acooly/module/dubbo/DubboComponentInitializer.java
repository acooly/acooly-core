/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-10 11:23 创建
 *
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/** @author qiubo@yiji.com */
public class DubboComponentInitializer implements ComponentInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    //tuning dubbo logger ,avoid log4j initialize
    System.setProperty("dubbo.application.logger", "slf4j");
      Boolean dubboEnable=true;
      if(!applicationContext.getEnvironment().getProperty("acooly.dubbo.enable",Boolean.class,Boolean.TRUE)){
          dubboEnable=false;
      }
      if(!applicationContext.getEnvironment().getProperty("acooly.dubbo.provider.enable",Boolean.class,Boolean.TRUE)){
          dubboEnable=false;
      }
      System.setProperty("dubbo.provider.enable", dubboEnable.toString());
  }
}
