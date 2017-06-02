/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:08 创建
 */
package com.acooly.core.common.boot.component;

import com.acooly.core.common.boot.EnvironmentHolder;
import org.springframework.context.ConfigurableApplicationContext;

/** @author qiubo */
public interface ComponentInitializer extends AutoConfigExcluder {
  default void initialize(ConfigurableApplicationContext applicationContext) {}

  default void setPropertyIfMissing(String key, Object value) {
    if (!EnvironmentHolder.get().containsProperty(key)) {
      System.setProperty(key, value.toString());
    }
  }
}
