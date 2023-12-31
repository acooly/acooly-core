/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 22:32 创建
 */
package com.acooly.core.common.boot.listener;

import ch.qos.logback.classic.LoggerContext;
import com.acooly.core.AcoolyVersion;
import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.AcoolyBanner;
import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.boot.log.AcoolyLogbackLoggingSystem;
import com.acooly.core.common.boot.log.initializer.ConsoleLogInitializer;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.exception.UncaughtExceptionHandlerWrapper;
import com.acooly.core.utils.ShutdownHooks;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * acooly框架扩展
 *
 * @author zhangpu for v5
 */
@Slf4j
public class AcoolyApplicationRunListener implements SpringApplicationRunListener, PriorityOrdered {
    public static final Logger logger = LoggerFactory.getLogger(AcoolyApplicationRunListener.class);
    public static final String COMPONENTS_PACKAGE = "com.acooly.module";
    private static List<String> disabledPackageName = Lists.newArrayList("", "com.acooly", "com.acooly.core",
            "com.acooly.core.common.boot", COMPONENTS_PACKAGE);
    private boolean cloudEnv;
    private SpringApplication application;
    private String[] args;

    private StopWatch stopWatch = new StopWatch();

    public AcoolyApplicationRunListener(SpringApplication application, String[] args) {
        // application.getAllSources().contains()
        cloudEnv = Apps.checkCloudEnv(application);
        if (!cloudEnv) {
            this.application = application;
            this.args = args;
            stopWatch.start();
            setThreadName();
            application.setRegisterShutdownHook(false);
        }
    }


    /**
     * boot初始化扩展
     */
    @Override
    public void starting() {
        if (!cloudEnv) {
            BootApp bootApp = findBootApplication(application);
            initEnvVars(bootApp);
            checkAndSetPackage(application);
            checkCoreVersions();
            jvmPropstuning();
            application.setBanner(new AcoolyBanner());
        }
    }


    /**
     * 初始化EnvironmentHolder
     * <p>
     * 在environmentPrepared阶段会优先执行，在此阶段初始化EnvironmentHolder，可以用于日志系统配置时获取环境。
     * 在日志系统配置中使用此类时，获取的环境不包括hera PropertySource
     */
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        new EnvironmentHolder().setEnvironment(environment);
        setProfileIfEnableActiveProfiles(environment);
    }


    @Override
    public void started(ConfigurableApplicationContext context) {
        // in spring cloud context,don't do it
        if (!cloudEnv) {
            //install UncaughtExceptionHandler
            UncaughtExceptionHandlerWrapper.install();
            new ShutdownThread().register();
            // 只是用于独立统计信息
            stopWatch.stop();
            Apps.setStartupTimes(stopWatch.getTotalTimeMillis());
            //log startup info
            LoggerFactory.getLogger(AcoolyApplicationRunListener.class).info("启动成功: http://127.0.0.1:{}", context.getEnvironment().getProperty(Apps.HTTP_PORT));
        }
    }


    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        if (!cloudEnv) {
            ConsoleLogInitializer.addConsoleAppender();
            logger.error("启动失败: {}", exception.toString(), exception);
            System.err.println("启动失败: " + exception.toString());
            ShutdownHooks.shutdownAll();
            shutdownLogSystem();
        }
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        if (!cloudEnv) {
            Apps.report();
        }
    }


    private static void shutdownLogSystem() {
        //关闭日志，日志需要最后关闭,等所有资源清理后关闭日志
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();
    }

    /**
     * 关闭应用
     */
    public static void shutdownApp() {
        Logger logger = LoggerFactory.getLogger(AcoolyApplicationRunListener.class);
        logger.info("应用[{}]开始关闭", Apps.getAppName());
        //在应用关闭时打印console log,便于自动化发布系统查看日志
        ConsoleLogInitializer.addConsoleAppender();
        //close spring container
        ApplicationContext applicationContext = ApplicationContextHolder.get();
        if (applicationContext instanceof ConfigurableApplicationContext) {
            if (!((ConfigurableApplicationContext) applicationContext).isActive()) {
                System.exit(0);
            } else {
                ((ConfigurableApplicationContext) applicationContext).close();
            }
        }
        ShutdownHooks.shutdownAll();
        shutdownLogSystem();
    }

    /**
     * 检查核心依赖版本 目前主要为： 1、acooly版本与spring的主版本一致 2、JDK大于1.8以上
     */
    private void checkCoreVersions() {
        if (!SpringVersion.getVersion().startsWith(AcoolyVersion.getMajorVersion())) {
            throw new AppConfigException("请确保org.springframework:spring-*版本与Acooly版本(" + AcoolyVersion.getVersion() + ")匹配");
        }
        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8)) {
            throw new AppConfigException("请使用jdk1.8及以上版本");
        }

    }

    private void checkAndSetPackage(SpringApplication application) {
        application.getAllSources().forEach(o -> {
            Package pkg = ((Class<?>) o).getPackage();
            if (pkg == null || disabledPackageName.contains(pkg.getName())) {
                throw new AppConfigException("请把main-class定义到应用包中，禁止定义到以下包中:" + disabledPackageName);
            }
            System.setProperty(Apps.BASE_PACKAGE, pkg.getName());
        });
    }

    private void setThreadName() {
        Thread.currentThread().setName("main");
    }

    private void initEnvVars(BootApp bootApp) {
        if (bootApp != null) {
            // 如果设置了bootApp，使用该配置覆盖springboot的标准配置。System.setProperty优先级大于.properties和yml的配置
            System.setProperty(Apps.APP_NAME, bootApp.sysName());
            System.setProperty("server.servlet.application-display-name", bootApp.sysName());
            System.setProperty(Apps.HTTP_PORT, Integer.toString(bootApp.httpPort()));
        }
        //set servelt container response header server
        System.setProperty("server.server-header", "ACOOLY");
        //for extends log
//        String logPath = Apps.getLogPath();
//        System.setProperty(Apps.LOG_PATH, logPath);
        System.setProperty(LoggingSystem.SYSTEM_PROPERTY, AcoolyLogbackLoggingSystem.class.getName());
        //spring aop use cglib
        System.setProperty("spring.aop.proxy-target-class", Boolean.TRUE.toString());
        // 如果有加載devtools依賴，则关闭。
        System.setProperty("spring.devtools.restart.enabled", "false");
        // 独立线程设置PID
        new Runnable() {
            @Override
            public void run() {
                System.setProperty(Apps.PID, new ApplicationPid().toString());
            }
        };
    }

    private BootApp findBootApplication(SpringApplication application) {
        Class<?> sourceClass =
                application
                        .getAllSources()
                        .stream()
                        .map(o1 -> (Class<?>) o1)
                        .filter(o1 -> AnnotationUtils.findAnnotation(o1, BootApp.class) != null)
                        .findFirst().orElseGet(() -> {
                            return null;
                        });
        if (sourceClass == null) {
            return null;
        }
        return AnnotationUtils.findAnnotation(sourceClass, BootApp.class);
    }

    private void jvmPropstuning() {
        //优选ipv4网络
        System.setProperty("java.net.preferIPv4Stack", "true");
        //rmi gc 间隔
        System.setProperty("sun.rmi.dgc.client.gcInterval", "7200000");
        System.setProperty("sun.rmi.dgc.server.gcInterval", "7200000");
        //启用headless模式
        System.setProperty("java.awt.headless", "true");
        System.setProperty("java.security.egd", "file:/dev/./urandom");
    }

    private void setProfileIfEnableActiveProfiles(ConfigurableEnvironment environment) {
        if (Strings.isNullOrEmpty(System.getProperty(Apps.SPRING_PROFILE_ACTIVE))) {
            String profile = environment.getProperty(Apps.SPRING_PROFILE_ACTIVE);
            if (!Strings.isNullOrEmpty(profile)) {
                System.setProperty(Apps.SPRING_PROFILE_ACTIVE, profile);
            }
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 2;
    }


    static class ShutdownThread extends Thread {

        public ShutdownThread() {
            super("Shutdown");
        }

        @Override
        public void run() {
            shutdownApp();
        }

        public void register() {
            Runtime.getRuntime().addShutdownHook(this);
        }
    }
}
