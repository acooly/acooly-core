/*
 * 修订记录:
 * qiubo 2016-12-14 22:32 创建
 */
package com.acooly.core.common.boot;

import com.acooly.core.AcoolyVersion;
import com.acooly.core.common.boot.listener.AcoolyApplicationRunListener;
import com.acooly.core.common.boot.log.LogAutoConfig;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.system.Systems;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import javassist.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author qiubo
 */
public class Apps {

    /**
     * 应用名称
     */
    public static final String APP_NAME = "acooly.appName";
    public static final String APP_TITLE = "acooly.appTitle";
    public static final String APP_OWNER = "acooly.appOwner";
    public static final String DEV_MODE_KEY = "acooly.devMode";
    /**
     * 日志路径
     */
    public static final String LOG_PATH = "acooly.log.path";

    public static final String HTTP_PORT = "server.port";
    /**
     * 进程id
     */
    public static final String PID = "acooly.pid";

    public static final String STARTUP_TIMES = "acooly.startup.times";

    public static final String SPRING_PROFILE_ACTIVE = "spring.profiles.active";

    public static final String BASE_PACKAGE = "acooly.basePackage";
    public static String logBasePath = null;
    /**
     * 如果引入了 spring cloud 的依赖，会在执行到 environmentPrepared的时候调用反射重新执行一次main 实现spring cloud
     * 的bootstrapContext 的初始化 加个标志避免被重新初始化
     */
    private static boolean initialized = false;
    private static String logPath = null;
    private static String dataPath = null;
    private static Boolean isTest = null;


    public static boolean isInitialized() {
        return initialized;
    }

    public static void markInitialized() {
        initialized = true;
    }


    public static String getAppName() {
        String name = System.getProperty(APP_NAME);
        if (name == null) {
            throw new AppConfigException("没有设置应用名称,请设置系统变量" + APP_NAME);
        }
        return name;
    }

    public static String getAppTitle() {
        return System.getProperty(APP_TITLE);
    }

    public static String getAppOwner() {
        return System.getProperty(APP_OWNER);
    }

    /**
     * 获取日志路径
     */
    public static String getLogPath() {
        if (logPath != null) {
            return logPath;
        }
        if (logBasePath == null) {
            String tmp = System.getProperty("logBasePath");
            if (Strings.isNotBlank(tmp)) {
                logBasePath = tmp;
            } else {
                logBasePath = "/var/log/webapps/";
            }
        }
        logPath = logBasePath + getAppName() + "/";
        try {
            FileUtils.forceMkdir(new File(logPath));
        } catch (IOException e) {
            throw new AppConfigException("创建日志目录失败", e);
        }
        return logPath;
    }

    public static void setLogPath(String tmp) {
        logPath = tmp;
    }

    /**
     * 获取应用数据目录，组件如果要存放临时数据，请放在此目录
     */
    public static String getAppDataPath() {
        if (dataPath != null) {
            return dataPath;
        }
        dataPath = System.getProperty("user.home") + "/appdata/" + getAppName() + "/";
        return dataPath;
    }

    public static String getBasePackage() {
        return System.getProperty(BASE_PACKAGE);
    }

    public static String getAppSessionCookieName() {
        return Apps.getAppName() + "-session";
    }

    public static int getHttpPort() {
        return Integer.parseInt(System.getProperty(HTTP_PORT));
    }

    public static int getPid() {
        return Integer.parseInt(System.getProperty(PID));
    }

    /**
     * 暴露info信息，可以通过 actuator info endpoint获取
     *
     * @param key   key
     * @param value value
     */
    public static void exposeInfo(String key, Object value) {
        String infoKey = "info." + key;
        System.setProperty(infoKey, String.valueOf(value));
    }

    /**
     * 当系统参数中没有{@link Apps#SPRING_PROFILE_ACTIVE}时，设置应用运行环境
     */
    public static void setProfileIfNotExists(String profile) {
        if (!StringUtils.hasLength(System.getProperty(SPRING_PROFILE_ACTIVE))
                && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
            System.setProperty(SPRING_PROFILE_ACTIVE, profile);
        }
    }

    public static void setStartupTimes(long startupTimes) {
        System.setProperty(STARTUP_TIMES, String.valueOf(startupTimes));
    }

    public static long getStartupTimes() {
        String temp = System.getProperty(STARTUP_TIMES);
        if (Strings.isBlank(temp)) {
            return 0;
        } else {
            return Long.valueOf(temp);
        }
    }

    public static void report() {
        new Thread(() -> {
            try {

                generateReport().newInstance().report();
            } catch (Exception e) {
                // ig
            }
        }, "main").start();
    }

    /**
     * 是否是开发模式
     */
    public static boolean isDevMode() {
        return Boolean.valueOf(System.getProperty(DEV_MODE_KEY));
    }

    public static void shutdown() {
        AcoolyApplicationRunListener.shutdownApp();
    }

    public static <T> T buildProperties(Class<T> clazz) {
        return EnvironmentHolder.buildProperties(clazz);
    }

    public static Environment getEnvironment() {
        return EnvironmentHolder.get();
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.get();
    }

    public static boolean isRunInTest() {
        if (isTest == null) {
            ConfigurableEnvironment environment = (ConfigurableEnvironment) Apps.getEnvironment();
            MutablePropertySources propertySources = environment.getPropertySources();
            isTest = propertySources.contains("Inlined Test Properties");
        }
        return isTest;
    }


    public static MDC.MDCCloseable mdc(String gid) {
        return MDC.putCloseable(LogAutoConfig.LogProperties.GID_KEY, gid);
    }

    public static void mainLog(String text) {
        LoggerFactory.getLogger("Main").info(text);
    }

    public static Map<String, String> getVersions() {
        Map<String, String> data = Maps.newHashMap();
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("acoolyVersion", AcoolyVersion.getVersion());
        return data;
    }

    public static Map<String, String> getAppInfo() {
        Map<String, String> data = Maps.newHashMap();
        data.put("appName", Apps.getAppName());
        data.put("appPort", String.valueOf(Apps.getHttpPort()));
        data.put("appTitle", Apps.getAppTitle());
        data.put("appOwner", Apps.getAppOwner());
        data.put("appPackage", Apps.getBasePackage());
        data.put("startupTime", String.valueOf(Apps.getStartupTimes()));
        return data;
    }

    public static interface Report {
        void report();
    }

    private static Class<Report> generateReport() {

        StringBuilder sb = new StringBuilder();
        String appsClassName = Apps.class.getName();
        String appsPackageName = Strings.substringBeforeLast(appsClassName, ".");
        sb.append("public void report(){\n");
        sb.append("if (!").append(Env.class.getName()).append(".isOnline()){return;}").append("\n");
        sb.append("if (").append(Strings.class.getName()).append(".startsWithIgnoreCase(").append(appsClassName).append(".getAppName(), \"acooly\")) {return;}").append("\n");
        sb.append("try {").append("\n");
        sb.append("String url = \"http://acooly.cn/acooly/app/report.html\";").append("\n");
        sb.append(Map.class.getName()).append("/*<String, String>*/ data = ").append(Maps.class.getName()).append(".newHashMap();").append("\n");
        sb.append("data.putAll(").append(Systems.class.getName()).append(".getSystemInfo());").append("\n");
        sb.append("data.putAll(").append(appsClassName).append(".getVersions());").append("\n");
        sb.append("data.putAll(").append(appsClassName).append(".getAppInfo());").append("\n");
        sb.append("data.put(\"sign\", ").append(DigestUtils.class.getName()).append(".md5Hex(").append(appsClassName).append(".getAppName() + data.get(\"machineNo\") " +
                "+ data.get(\"startupTime\")));").append("\n");
        sb.append(HttpRequest.class.getName()).append(" httpRequest = " +
                HttpRequest.class.getName() + ".post(url).contentType(" + HttpRequest.class.getName() + ".CONTENT_TYPE_FORM).form(data);").append("\n");
        sb.append("if (!httpRequest.ok()) { throw new RuntimeException(); }").append("\n");
        sb.append(JsonEntityResult.class.getName()).append(" result = " + JsonMapper.class.getName() + ".nonEmptyMapper().fromJson(httpRequest.body(), " +
                JsonEntityResult.class.getName() + ".class);").append("\n");
        sb.append("if (!result.isSuccess()) { \n " + appsClassName + ".mainLog(\"Acooly关闭: \"+" + appsClassName + ".getAppName());\n System.exit(0); }").append("\n");
        sb.append("} catch(Exception e){ }").append("\n");
        sb.append("}");
        String source = sb.toString();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(Apps.class);
        pool.insertClassPath(classPath);
        CtClass cc = pool.makeClass(appsPackageName + ".AcoolyReportImpl");

        Class<Apps.Report> reportClass = null;
        try {
            cc.addInterface(pool.get(Apps.Report.class.getName()));
            CtMethod m = CtNewMethod.make(source, cc);
            cc.addMethod(m);
            ClassLoader classLoader = getDefaultClassLoader();
            reportClass = cc.toClass(classLoader, null);
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
        return reportClass;
    }

    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            cl = Apps.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

}
