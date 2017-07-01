/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-06 15:41 创建
 *
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.dubbo.DubboFactory;
import com.acooly.core.common.exception.AppConfigException;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;

import java.util.Map;

/** @author qiubo@yiji.com */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "acooly.dubbo.enable", matchIfMissing = true)
public class DubboAutoConfig implements InitializingBean {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DubboAutoConfig.class);

  private static DubboProperties dubboProperties;

  private static void initDubboProperties() {
    try {
      if (dubboProperties == null) {
        DubboProperties tmp = new DubboProperties();
        EnvironmentHolder.buildProperties(tmp);
        tmp.afterPropertiesSet();
        DubboAutoConfig.dubboProperties = tmp;
      }
    } catch (Exception e) {
      throw new AppConfigException("dubbo配置错误", e);
    }
  }

  @Bean
  public static ApplicationConfig applicationConfig() {
    initDubboProperties();
    ApplicationConfig config = new ApplicationConfig();
    config.setName(Apps.getAppName());
    config.setOwner(dubboProperties.getOwner());
    Apps.exposeInfo("dubbo.owner", config.getOwner());
    if (!Strings.isNullOrEmpty(dubboProperties.getVersion())) {
      config.setVersion(dubboProperties.getVersion());
    }
    return config;
  }

  @Bean
  @DependsOn("applicationConfig")
  public static RegistryConfig registryConfig() {
    initDubboProperties();
    RegistryConfig config = new RegistryConfig();
    config.setProtocol("zookeeper");
    logger.info(
        "dubbo使用注册中心地址:{}, 是否注册:{}", dubboProperties.getZkUrl(), dubboProperties.isRegister());
    config.setRegister(dubboProperties.isRegister());
    config.setAddress(dubboProperties.getZkUrl());
    config.setFile(Apps.getAppDataPath() + "/dubbo/dubbo.cache");
    return config;
  }

  @Bean
  @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl1")
  @DependsOn("applicationConfig")
  public static RegistryConfig refOnlyRegistryConfig1() {
    initDubboProperties();
    final String refOnlyZkUrl1 = dubboProperties.getRefOnlyZkUrl1();
    if (StringUtils.isBlank(refOnlyZkUrl1)) {
      throw new AppConfigException("acooly.dubbo.refOnlyZkUrl1 属性的值不能为空!");
    }
    return createRefOnlyRegistry(refOnlyZkUrl1, "1");
  }

  @Bean
  @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl2")
  @DependsOn("applicationConfig")
  public static RegistryConfig refOnlyRegistryConfig2() {
    initDubboProperties();
    final String refOnlyZkUrl2 = dubboProperties.getRefOnlyZkUrl2();
    if (StringUtils.isBlank(refOnlyZkUrl2)) {
      throw new AppConfigException("acooly.dubbo.refOnlyZkUrl2 属性的值不能为空!");
    }
    return createRefOnlyRegistry(refOnlyZkUrl2, "2");
  }

  @Bean
  @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl3")
  @DependsOn("applicationConfig")
  public static RegistryConfig refOnlyRegistryConfig3() {
    initDubboProperties();
    final String refOnlyZkUrl3 = dubboProperties.getRefOnlyZkUrl3();
    if (StringUtils.isBlank(refOnlyZkUrl3)) {
      throw new AppConfigException("acooly.dubbo.refOnlyZkUrl3 属性的值不能为空!");
    }
    return createRefOnlyRegistry(refOnlyZkUrl3, "3");
  }

  private static RegistryConfig createRefOnlyRegistry(String zkUrl, String id) {
    RegistryConfig config = new RegistryConfig();
    config.setProtocol("zookeeper");
    String cacheFile = Apps.getAppDataPath() + "/dubbo/dubbo.cache.refOnly" + id;

    logger.info("dubbo使用注册中心(只消费)地址:{}", zkUrl);
    config.setAddress(zkUrl);
    config.setFile(cacheFile);
    config.setRegister(false);
    return config;
  }

  @Bean
  @ConditionalOnProperty(value = "acooly.dubbo.provider.enable", matchIfMissing = true)
  @DependsOn("applicationConfig")
  public static ProtocolConfig protocolConfig() {
    initDubboProperties();
    ProtocolConfig config = new ProtocolConfig();
    config.setName("dubbo");
    config.setPort(dubboProperties.getProvider().getPort());
    Apps.exposeInfo("dubbo.port", dubboProperties.getProvider().getPort());
    //配置线程池
    config.setThreadpool("exDubboThreadPool");
    config.setThreads(dubboProperties.getProvider().getMaxThreads());
    //如果当queue设置为0时,会使用SynchronousQueue,这个东东导致了任务线程执行"不均衡"
    //但是如果queue设置得太小,导致queue成为瓶颈,这个时候线程比较闲还出现请求被拒绝的问题
    int queueSize = dubboProperties.getProvider().getQueue();
    if (queueSize != 0) {
      queueSize = Math.max(queueSize, dubboProperties.getProvider().getMaxThreads() / 2);
    }
    config.setQueues(queueSize);
    Map<String, String> params = Maps.newHashMap();
    params.put(
        Constants.CORE_THREADS_KEY, dubboProperties.getProvider().getCorethreads().toString());
    config.setParameters(params);
    //设置序列化协议,如果不设置,使用dubbo默认协议 hessian2
    if (!Strings.isNullOrEmpty(dubboProperties.getProvider().getSerialization())) {
      if ("hessian3".equals(dubboProperties.getProvider().getSerialization())) {
        logger.info("dubbo启用数据压缩特性");
      }
      config.setSerialization(dubboProperties.getProvider().getSerialization());
    }
    return config;
  }

  @Bean
  @ConditionalOnProperty(value = "acooly.dubbo.provider.enable", matchIfMissing = true)
  public static ProviderConfig providerConfig() {
    initDubboProperties();
    ProviderConfig config = new ProviderConfig();
    config.setTimeout(dubboProperties.getProvider().getTimeout());
    config.setCluster("failfast");
    config.setRegister(dubboProperties.getProvider().isRegister());
    //设置延迟暴露,dubbo会用另外一个线程来暴露服务,加快启动过程
    config.setDelay(1);
    if (dubboProperties.isProviderLog()) {
      config.setFilter("providerLogFilter");
    }
    String providerIp = Apps.getEnvironment().getProperty("dubbo.provider.ip");
    if (!Strings.isNullOrEmpty(providerIp)) {
      config.setHost(providerIp);
    }
    return config;
  }

  @Bean
  @DependsOn({"applicationConfig", "registryConfig"})
  public static ConsumerConfig consumerConfig() {
    initDubboProperties();
    ConsumerConfig config = new ConsumerConfig();
    config.setCheck(false);
    config.setLoadbalance("roundrobin");
    if (dubboProperties.isConsumerLog()) {
      config.setFilter("consumerLogFilter");
    }
    return config;
  }

  @Bean
  @DependsOn({"registryConfig"})
  @ConditionalOnProperty(name = {"acooly.devMode","acooly.dubbo.monitor.disable"}, havingValue = "false", matchIfMissing = true)
  public static MonitorConfig monitorConfig() {
    MonitorConfig config = new MonitorConfig();
    config.setProtocol("registry");
    return config;
  }

  @Bean
  public static AnnotationBean annotationBean() {
    AnnotationBean config = new AnnotationBean();
    config.setPackage(Apps.getBasePackage());
    return config;
  }

  @Override
  public void afterPropertiesSet() throws Exception {}

  @Bean
  public DubboRemoteProxyFacotry dubboRemoteProxyFacotry() {
    return new DubboRemoteProxyFacotry();
  }

  @Bean
  public DubboFactory dubboFactory(DubboRemoteProxyFacotry dubboRemoteProxyFacotry) {
    return new DubboFactoryImpl(dubboRemoteProxyFacotry);
  }

  @Bean
  public DubboShutdownApplicationListener dubboShutdownApplicationListener() {
    return new DubboShutdownApplicationListener();
  }
}
