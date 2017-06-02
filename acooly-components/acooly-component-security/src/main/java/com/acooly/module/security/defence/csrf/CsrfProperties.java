/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-25 16:21 创建
 *
 */
package com.acooly.module.security.defence.csrf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author qiubo */
@ConfigurationProperties("yiji.csrf")
public class CsrfProperties {

  /** 是否启用CSRF过滤 */
  private boolean enable = true;

  /** 可选：crsf忽略的uri，多个uri用逗号隔开，支持ant路径模式 */
  private String ignoreUris;

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getIgnoreUris() {
    return ignoreUris;
  }

  public void setIgnoreUris(String ignoreUris) {
    this.ignoreUris = ignoreUris;
  }
}
