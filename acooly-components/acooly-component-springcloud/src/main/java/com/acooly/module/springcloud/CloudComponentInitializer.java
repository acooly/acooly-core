package com.acooly.module.springcloud;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class CloudComponentInitializer implements ComponentInitializer {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    //关闭retry 机制
    System.setProperty("ribbon.ConnectTimeout", "600");
    System.setProperty("spring.cloud.loadbalancer.retry.enabled", "false");
    System.setProperty("ribbon.OkToRetryOnAllOperations", "false");
    System.setProperty("ribbon.MaxAutoRetriesNextServer", "0");
    System.setProperty("ribbon.MaxAutoRetries", "0");
    System.setProperty("ribbon.ReadTimeout", "6000");
    //默认使用httpClient
    System.setProperty("feign.httpclient.enabled", "true");
  }
}
