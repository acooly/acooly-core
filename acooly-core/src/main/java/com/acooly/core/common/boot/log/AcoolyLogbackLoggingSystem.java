/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-12 17:43 创建
 *
 */
package com.acooly.core.common.boot.log;

import ch.qos.logback.classic.LoggerContext;
import com.acooly.core.common.boot.AcoolyBanner;
import com.acooly.core.common.boot.listener.DevModeDetector;
import com.acooly.core.common.boot.log.initializer.LogInitializer;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;

import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * @author qiubo
 * @author zhangpu for v5
 */
@SuppressWarnings("all")
public class AcoolyLogbackLoggingSystem extends LogbackLoggingSystem {
    private ClassLoader classLoader;

    public AcoolyLogbackLoggingSystem(ClassLoader classLoader) {
        super(classLoader);
        this.classLoader = classLoader;
    }

    @Override
    public void initialize(
            LoggingInitializationContext initializationContext, String configLocation, LogFile logFile) {
        super.initialize(initializationContext, configLocation, logFile);
    }

    /**
     * 没有logback日志配置文件调用此方法
     *
     * @param logFile
     */
    @Override
    protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
        new DevModeDetector().apply((ConfigurableEnvironment) initializationContext.getEnvironment());
        LoggerContext context = getLoggerContext();
        context.stop();
        context.reset();
        LogbackConfigurator configurator =
                new LogbackConfigurator(context, this.classLoader, initializationContext.getEnvironment());
        customLog(configurator);
    }

    private void customLog(LogbackConfigurator configurator) {
        //config log extension
        List<LogInitializer> logInitializers =
                SpringFactoriesLoader.loadFactories(LogInitializer.class, getClassLoader());
        for (LogInitializer logInitializer : logInitializers) {
            logInitializer.init(configurator);
        }
        AcoolyBanner.getInfos().addAll(configurator.getLogs());
    }

    /**
     * 系统中有日志配置文件，调用此方法
     *
     * @param location
     * @param logFile
     */
    @Override
    protected void loadConfiguration(
            LoggingInitializationContext initializationContext, String location, LogFile logFile) {
        LoggerContext loggerContext = getLoggerContext();
        //渲染异常日志时，忽略的栈
        System.setProperty(
                "acooly.log.ignoredStackFrames",
                "java.lang.reflect.Method,"
                        + "org.apache.catalina,"
                        + "sun.reflect,"
                        + "net.sf.cglib,"
                        + "org.springframework.aop,"
                        + "org.springframework.web.filter.OncePerRequestFilter,"
                        + "org.springframework.web.servlet,"
                        + "com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper");

        super.loadConfiguration(initializationContext, location, logFile);
        LogbackConfigurator configurator =
                new LogbackConfigurator(
                        loggerContext, this.classLoader, initializationContext.getEnvironment());
        configurator.log("加载日志配置文件:%s", location);
        customLog(configurator);
    }

    private LoggerContext getLoggerContext() {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Assert.isInstanceOf(
                LoggerContext.class,
                factory,
                String.format(
                        "LoggerFactory is not a Logback LoggerContext but Logback is on "
                                + "the classpath. Either remove Logback or the competing "
                                + "implementation (%s loaded from %s). If you are using "
                                + "Weblogic you will need to add 'org.slf4j' to "
                                + "prefer-application-packages in WEB-INF/weblogic.xml",
                        factory.getClass(), getLocation(factory)));
        return (LoggerContext) factory;
    }

    private Object getLocation(ILoggerFactory factory) {
        try {
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException ex) {
            // Unable to determine location
        }
        return "unknown location";
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    @Override
    public void beforeInitialize() {
        super.beforeInitialize();
    }
}
