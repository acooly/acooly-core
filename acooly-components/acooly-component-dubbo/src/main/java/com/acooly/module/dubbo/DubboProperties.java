/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-20 16:14 创建
 *
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.boot.Apps;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.List;

/** @author qiubo@yiji.com */
@ConfigurationProperties(DubboProperties.PREFIX)
@Data
public class DubboProperties implements InitializingBean {
  public static final String PREFIX = "acooly.dubbo";

  /** 是否启用dubbo */
  private boolean enable = true;

  /** 是否注册本应用的服务到注册中心(测试的时候可能需要本地服务不注册到注册中心) */
  private boolean register = true;

  private String zkUrl = "127.0.0.1:2181";

  /** 只消费注册中心zk地址 */
  private String refOnlyZkUrl1 = "";

  private String refOnlyZkUrl2 = "";
  private String refOnlyZkUrl3 = "";

  /** 必填：应用负责人,请填写邮箱前缀 */
  private String owner;

  /**   选填：应用版本号，如果配置此版本号，服务可以不用指定版本号 */
  private String version;

  /** 是否启用dubbo consumer请求provider日志 */
  private boolean consumerLog = true;

  /** 是否启用dubbo provider提供服务被请求日志 */
  private boolean providerLog = true;

  private Provider provider = new Provider();

  private Consumer consumer = new Consumer();

  /**
   * dubbo 可自定义增加注解扫描路径，用,分割，此路径下会扫描{@link Reference}，{@link Service}这两个注解，默认会扫描{@link
   * Apps#getBasePackage()}路径
   */
  private String cumstomConfigPackage;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (enable) {
      Assert.hasText(owner, "dubbo应用负责人acooly.dubbo.owner不能为空");
    }
  }

  @Data
  public static class Provider {
    public static final int DEFAULT_THREAD = 400;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final boolean DEFAULT_REGISTER = true;
    /** provider 序列化，使用数据压缩协议 */
    private String serialization = "hessian3";
    /** 是否启用服务提供者 */
    private boolean enable = true;
    /** 必填：服务提供者端口 */
    private Integer port;
    /** 线程池最大线程数 */
    private Integer maxThreads = DEFAULT_THREAD;
    /** 初始化核心线程数 */
    private Integer corethreads = 50;
    /** 队列大小 */
    private Integer queue = 0;
    /** 服务超时时间，默认60s */
    private Integer timeout = DEFAULT_TIMEOUT;
    /** 服务是否注册到zk */
    private boolean register = DEFAULT_REGISTER;
  }
  @Data
  public static class Consumer{
    private List<String> mockInterfaces;
  }
}
