/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-29 14:39 创建
 */
package com.acooly.module.threadpool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.threadpool.ThreadPoolProperties.PREFIX;

/** @author qiubo@yiji.com */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class ThreadPoolProperties {
  public static final String PREFIX = "acooly.threadpool";
  private int threadMin = 10;
  private int threadMax = 100;
  private int threadQueue = 50;
}
