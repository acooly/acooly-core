/*
 * 修订记录:
 * qiubo 2016-12-14 22:32 创建
 */
package com.acooly.core.common.boot;

import com.acooly.core.AcoolyVersion;
import com.acooly.core.common.boot.listener.ExApplicationRunListener;
import com.acooly.core.common.boot.log.LogAutoConfig;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.Exceptions;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.core.utils.system.Systems;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
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

    public static final String DEV_MODE_KEY = "acooly.devMode";
    /**
     * 日志路径
     */
    public static final String LOG_PATH = "acooly.log.path";

    public static final String HTTP_PORT = "server.port";

    public static final String STARTUP_TIMES = "acooly.startup.times";
    /**
     * 进程id
     */
    public static final String PID = "acooly.pid";

    public static final String SPRING_PROFILE_ACTIVE = "spring.profiles.active";

    public static final String BASE_PACKAGE = "acooly.basePackage";
    public static String logBasePath = null;
    private static String logPath = null;
    private static String dataPath = null;
    private static Boolean isTest = null;

    public static String getAppName() {
        String name = System.getProperty(APP_NAME);
        if (name == null) {
            throw new AppConfigException("没有设置应用名称,请设置系统变量" + APP_NAME);
        }
        return name;
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
        if (!Env.isOnline()) {
            return;
        }
        if (Strings.startsWithIgnoreCase(Apps.getAppName(), "acooly")) {
            return;
        }
        new Thread(() -> {
            try {
                String url = "http://acooly.cn/acooly/app/report.html";
                Map<String, String> data = Maps.newHashMap();
                data.put("appName", Apps.getAppName());
                data.put("appPort", String.valueOf(Apps.getHttpPort()));
                data.put("machineNo", Systems.getSystemId());
                data.put("internalIp", IPUtil.getFirstNoLoopbackIPV4Address());
                data.put("hostName", Systems.getHostName());
                Systems.OsPlatform platform = Systems.getOS();
                data.put("osName", platform.getOs().name() + "_" + platform.getArch());
                data.put("osVersion", platform.getVersion());
                data.put("javaVersion", System.getProperty("java.version"));
                data.put("acoolyVersion", AcoolyVersion.getVersion());
                data.put("startupTime", String.valueOf(Apps.getStartupTimes()));
                data.put("sign", DigestUtils.md5Hex(Apps.getAppName() + data.get("machineNo") + data.get("startupTime")));
                HttpRequest httpRequest = HttpRequest.post(url).contentType(HttpRequest.CONTENT_TYPE_FORM).form(data);
                if (!httpRequest.ok()) {
                    Exceptions.runtimeException("通讯失败");
                }
                String responseBody = httpRequest.body();
                JsonEntityResult result = JsonMapper.nonEmptyMapper().fromJson(responseBody, JsonEntityResult.class);
                if (!result.isSuccess()) {
                    Exceptions.rethrow(result.getCode(), result.getMessage());
                }
            } catch (Exception e) {
                // ig
            }
        }).start();
    }

    /**
     * 是否是开发模式
     */
    public static boolean isDevMode() {
        return Boolean.valueOf(System.getProperty(DEV_MODE_KEY));
    }

    public static void shutdown() {
        ExApplicationRunListener.shutdownApp();
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
}
