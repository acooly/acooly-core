/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-06 15:41 创建
 *
 */
package com.acooly.module.tomcat;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.exception.AppConfigException;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.JspServlet;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** @author qiubo */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Tomcat.class)
@EnableConfigurationProperties({TomcatProperties.class})
public class TomcatAutoConfig {
  private static final Logger logger = LoggerFactory.getLogger(TomcatAutoConfig.class);

  @Autowired private TomcatProperties tomcatProperties;

  @Bean(name = "embeddedServletContainerCustomizer")
  public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
    return container -> {
      // 1. disable jsp if possible
      if (!EnvironmentHolder.get()
          .getProperty("acooly.web.jsp.enable", Boolean.class, Boolean.TRUE)) {
        JspServlet jspServlet = new JspServlet();
        jspServlet.setRegistered(false);
        container.setJspServlet(jspServlet);
      } else {
        JspServlet jspServlet = new JspServlet();
        Map<String, String> param = Maps.newHashMap();
        param.put("compilerTargetVM", "1.8");
        param.put("compilerSourceVM", "1.8");
        if (Apps.isDevMode()) {
          param.put("development", Boolean.TRUE.toString());
        } else {
          param.put("development", Boolean.FALSE.toString());
        }
        jspServlet.setInitParameters(param);
        container.setJspServlet(jspServlet);
      }

      // 2. 定制tomcat
      if (container instanceof TomcatEmbeddedServletContainerFactory) {
        TomcatEmbeddedServletContainerFactory factory =
            (TomcatEmbeddedServletContainerFactory) container;
        factory.setUriEncoding(Charset.forName(tomcatProperties.getUriEncoding()));
        setTomcatWorkDir(factory);
        // 2.1 设置最大线程数为400
        factory.addConnectorCustomizers(
            (TomcatConnectorCustomizer)
                connector -> {
                  ProtocolHandler handler = connector.getProtocolHandler();
                  if (handler instanceof AbstractProtocol) {
                    @SuppressWarnings("rawtypes")
                    AbstractProtocol protocol = (AbstractProtocol) handler;
                    protocol.setMaxThreads(tomcatProperties.getMaxThreads());
                    protocol.setMinSpareThreads(tomcatProperties.getMinSpareThreads());
                  }
                  connector.setAttribute("acceptCount", "100");
                });
        // 2.2 设置访问日志目录和日志格式
        if (tomcatProperties.isAccessLogEnable()) {
          if (factory
              .getContextValves()
              .stream()
              .anyMatch((valve) -> valve instanceof AccessLogValve)) {
            throw new AppConfigException("AccessLogValve已经配置，请不要启用默认spring-boot AccessLogValve配置");
          }
          AccessLogValve valve = new AccessLogValve();
          // 参数含义参考AbstractAccessLogValve 注释
          valve.setPattern(TomcatProperties.HTTP_ACCESS_LOG_FORMAT);
          valve.setSuffix(".log");
          // 读取真实ip，参考：org.apache.catalina.valves.AbstractAccessLogValve.HostElement
          valve.setRequestAttributesEnabled(true);
          valve.setDirectory(Apps.getLogPath());
          factory.addContextValves(valve);
        }
        // 2.3 设置错误页面
        setErrorPage(container);

        // fix https://github.com/spring-projects/spring-boot/issues/9670
        if (!Apps.isDevMode()) {
          factory.addContextLifecycleListeners(
              event -> {
                if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                  ((StandardContext) event.getLifecycle())
                      .getResources()
                      .setCacheTtl(100l * 24 * 60 * 60 * 1000);
                }
              });
        }
      }
    };
  }

  private void setTomcatWorkDir(TomcatEmbeddedServletContainerFactory factory) {
    // 设置tomcat base dir
    File file = new File(Apps.getAppDataPath() + "/tomcat-" + Apps.getHttpPort());
    file.mkdirs();
    factory.setBaseDirectory(file);
    file.deleteOnExit();
    // 设置tomcat doc base dir
    File docbase = new File(Apps.getAppDataPath() + "/tomcat-docbase-" + Apps.getHttpPort());
    docbase.mkdirs();
    factory.setDocumentRoot(docbase);
    docbase.deleteOnExit();
    logger.info("设置tomcat baseDir={},docbase={}", file, docbase);
  }

  private void setErrorPage(ConfigurableEmbeddedServletContainer container) {
    String error40XPage = tomcatProperties.getError40XPage();
    String error50XPage = tomcatProperties.getError50XPage();

    Set<ErrorPage> errorPages = new HashSet<>();

    HttpStatus[] values = HttpStatus.values();
    for (HttpStatus v : values) {
      int value = v.value();
      if (value >= 400 && value < 500) {
        if (!Strings.isNullOrEmpty(error40XPage)) {
          ErrorPage error40X = new ErrorPage(v, error40XPage);
          errorPages.add(error40X);
        }
      }
      if (value >= 500 && value < 600) {
        if (!Strings.isNullOrEmpty(error50XPage)) {
          ErrorPage error50X = new ErrorPage(v, error50XPage);
          errorPages.add(error50X);
        }
      }
    }
    if (!errorPages.isEmpty()) {
      container.setErrorPages(errorPages);
    }
  }
}
