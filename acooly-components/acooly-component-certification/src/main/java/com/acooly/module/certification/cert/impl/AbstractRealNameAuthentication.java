/*
 * 修订记录:
 * zhike@yiji.com 2017-04-21 10:21 创建
 *
 */
package com.acooly.module.certification.cert.impl;

import com.acooly.module.certification.CertificationProperties;
import com.acooly.module.certification.cert.RealNameAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */
public abstract class AbstractRealNameAuthentication
    implements RealNameAuthentication, InitializingBean {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected String appCode;
  protected String service;
  protected int timeout;

  @Autowired private CertificationProperties certificationProperties;

  @Override
  public void afterPropertiesSet() throws Exception {
    appCode = certificationProperties.getAppCode();
    service = certificationProperties.getUrl();
    timeout = certificationProperties.getTimeout();
  }
}
