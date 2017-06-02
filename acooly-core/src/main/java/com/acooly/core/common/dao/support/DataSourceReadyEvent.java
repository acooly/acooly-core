/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-26 10:41 创建
 */
package com.acooly.core.common.dao.support;

import org.springframework.context.ApplicationEvent;

import javax.sql.DataSource;

/** @author qiubo@yiji.com */
public class DataSourceReadyEvent extends ApplicationEvent {

  public DataSourceReadyEvent(DataSource source) {
    super(source);
  }
}
