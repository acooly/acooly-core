/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-04-12 23:33 创建
 */
package com.acooly.module.web;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/** @author qiubo */
public class WebComponentInitializer implements ComponentInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    setPropertyIfMissing("spring.http.multipart.maxFileSize", "50Mb");
    setPropertyIfMissing("spring.http.multipart.maxRequestSize", "300Mb");

    setPropertyIfMissing("spring.freemarker.expose-session-attributes", false);
    setPropertyIfMissing("spring.freemarker.expose-request-attributes", false);
    setPropertyIfMissing("spring.freemarker.allow-session-override", false);
    //org.springframework.web.servlet.support.RequestContext 对象在freemarker中的名称
    setPropertyIfMissing("spring.freemarker.request-context-attribute", "rc");

    // 设置session
    setPropertyIfMissing("server.session.cookie.name", Apps.getAppSessionCookieName());
    setPropertyIfMissing("server.session.cookie.httpOnly", Boolean.TRUE.toString());
    setPropertyIfMissing("server.session.tracking-modes", "cookie");
    setPropertyIfMissing("server.session.timeout", 7200);
    if (Apps.isDevMode()) {
      setPropertyIfMissing("spring.session.store-type", "HASH_MAP");
    } else {
      setPropertyIfMissing("spring.session.store-type", "REDIS");
    }
    setPropertyIfMissing("spring.redis.pool.max-active", 100);
    // 静态文件缓存
    if (!Apps.isDevMode()) {
      System.setProperty("spring.resources.cache-period", "-1");
    }
    // 设置freemarker
    setPropertyIfMissing("spring.freemarker.settings.classic_compatible", true);
    setPropertyIfMissing("spring.freemarker.settings.whitespace_stripping", true);
    setPropertyIfMissing("spring.freemarker.settings.locale", "zh_CN");
    setPropertyIfMissing("spring.freemarker.settings.default_encoding", "utf-8");
    setPropertyIfMissing("spring.freemarker.settings.url_escaping_charset", "utf-8");
    setPropertyIfMissing("spring.freemarker.settings.tag_syntax", "auto_detect");
    setPropertyIfMissing("spring.freemarker.settings.datetime_format", "yyyy-MM-dd HH:mm:ss");
    setPropertyIfMissing("spring.freemarker.settings.date_format", "yyyy-MM-dd");
    setPropertyIfMissing("spring.freemarker.settings.time_format", "HH:mm:ss");
    setPropertyIfMissing("spring.freemarker.settings.number_format", "0.######;");
    setPropertyIfMissing("spring.freemarker.settings.boolean_format", "true,false");

    // 设置 jackson
    setPropertyIfMissing("spring.jackson.date-format", "yyyy-MM-dd HH:mm:ss");
    setPropertyIfMissing("spring.jackson.time-zone", "Asia/Shanghai");
  }
}
