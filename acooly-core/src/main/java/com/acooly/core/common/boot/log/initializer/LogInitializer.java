/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-12 22:01 创建
 *
 */
package com.acooly.core.common.boot.log.initializer;

import com.acooly.core.common.boot.log.LogbackConfigurator;
import org.springframework.core.annotation.Order;

/**
 * log初始化扩展
 *
 * @author qiubo
 */
@Order(0)
public interface LogInitializer {
  void init(LogbackConfigurator configurator);
}
