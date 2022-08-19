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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qiubo
 * @author zhangpu
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Tomcat.class)
@EnableConfigurationProperties({TomcatProperties.class})
public class TomcatAutoConfig {
    private static final Logger logger = LoggerFactory.getLogger(TomcatAutoConfig.class);

    @Autowired
    private TomcatProperties tomcatProperties;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> acoolyWebServerFactoryCustomizer() {
        return factory -> {

            // 1. disable jsp if possible
            doSetJspConfig(factory);

            // 2. 定制tomcat工作空间
            doSetTomcatWorkDir(factory);

            // 3 设置访问日志目录和日志格式
            doSetAccessLog(factory);

            factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
                ProtocolHandler handler = connector.getProtocolHandler();
                if (handler instanceof AbstractProtocol) {
                    AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;
                    protocol.setMaxThreads(tomcatProperties.getMaxThreads());
                    protocol.setMinSpareThreads(tomcatProperties.getMinSpareThreads());
                }
                connector.setProperty("acceptCount", Integer.toString(tomcatProperties.getAcceptCount()));
            });

            // 添加tomcat的默认访问页
            factory.addContextCustomizers(context -> {
                context.addWelcomeFile("index.html");
                context.setBackgroundProcessorDelay((int) tomcatProperties.getBackgroundProcessorDelay().getSeconds());
            });

            // 禁用内置Tomcat的不安全请求方法
            if (tomcatProperties.getSecurity().isEnable()) {
                factory.addContextCustomizers(context -> {
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    // 开启认证
                    securityConstraint.setAuthConstraint(true);
                    SecurityCollection collection = new SecurityCollection();
                    // 对所有资源生效
                    collection.addPattern(tomcatProperties.getSecurity().getPattern());
                    // 以下是排除认证的非安全http方法（安全的方法）
                    List<String> omittedMethods = tomcatProperties.getSecurity().getOmittedMethods();
                    if (omittedMethods == null) {
                        omittedMethods = Lists.newArrayList();
                    }
                    if (omittedMethods.isEmpty()) {
                        omittedMethods.add("GET");
                        omittedMethods.add("get");
                        omittedMethods.add("post");
                        omittedMethods.add("POST");
                    }

                    for (String omittedMethod : omittedMethods) {
                        collection.addOmittedMethod(omittedMethod);
                    }

                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                });
            }
        };
    }

    protected void doSetJspConfig(TomcatServletWebServerFactory factory) {
        if (!EnvironmentHolder.get().getProperty("acooly.web.jsp.enable", Boolean.class, Boolean.TRUE)) {
            factory.getJsp().setRegistered(false);
        } else {
            Jsp jsp = factory.getJsp();
            Map<String, String> param = Maps.newHashMap();
            param.put("compilerTargetVM", "1.8");
            param.put("compilerSourceVM", "1.8");
            if (Apps.isDevMode()) {
                param.put("development", Boolean.TRUE.toString());
            } else {
                param.put("development", Boolean.FALSE.toString());
            }
            jsp.setInitParameters(param);
        }
    }


    /**
     * 自动设置专用的accessLog
     *
     * @param factory
     */
    protected void doSetAccessLog(TomcatServletWebServerFactory factory) {
        if (!tomcatProperties.isAccessLogEnable()) {
            return;
        }
        if (factory.getContextValves().stream().anyMatch((valve) -> valve instanceof AccessLogValve)) {
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


    private void doSetTomcatWorkDir(TomcatServletWebServerFactory factory) {
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

    /**
     * 设置配置的错误页面
     *
     * @param factory
     */
    private void doSetErrorPage(TomcatServletWebServerFactory factory) {
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
            factory.setErrorPages(errorPages);
        }
    }
}
