/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-25 16:19 创建
 *
 */
package com.acooly.module.tomcat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author qiubo */
@ConfigurationProperties("acooly.tomcat")
@Data
public class TomcatProperties {
  public static final String HTTP_ACCESS_LOG_FORMAT =
      "%h %l [%{yyyy-MM-dd HH:mm:ss.SSS}t] \"%r\" %s %b %D";
  /** 可选：最小空闲线程 */
  private int minSpareThreads = 20;
  /** 可选：最大线程数 */
  private volatile int maxThreads = 400;

  /** 可选：是否启用访问日志 */
  private boolean accessLogEnable = false;

  /** 可选: 设置uri编码 */
  private String uriEncoding = "UTF-8";

  /** 可选: 通过外部配置自定义tomcat端口 */
  private Integer port = null;

  /** 未授权页面401 */
  private String error401Page;
  /** 未找到页面404 */
  private String error404Page;
  /** 错误页面500 */
  private String error500Page;
}
