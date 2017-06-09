package com.acooly.module.obs.client;

import com.acooly.module.obs.ObsProperties;
import com.acooly.module.obs.client.ObsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/** @author shuijing */
public abstract class AbstractObsClient implements ObsClient, InitializingBean {
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected int timeout;

  @Autowired private ObsProperties obsProperties;

  @Override
  public void afterPropertiesSet() throws Exception {

    timeout = obsProperties.getTimeout();
  }
}
