/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 22:34 创建
 */
package com.acooly.core.common.exception;

/**
 * 应用配置异常
 *
 * @author qiubo
 */
public class AppConfigException extends BusinessException {

    public static final String APP_CONFIG_ERROR_CODE = "APP_CONFIG_ERROR";
    public static final String APP_CONFIG_ERROR_MESSAGE = "应用配置错误";

    public AppConfigException(Throwable cause) {
        super(APP_CONFIG_ERROR_CODE, APP_CONFIG_ERROR_MESSAGE, cause);
    }

    public AppConfigException(String message) {
        super(APP_CONFIG_ERROR_CODE, message, "");
    }

    public AppConfigException(String message, Throwable cause) {
        super(APP_CONFIG_ERROR_CODE, message, cause);
    }

    public static void throwIt(String message) {
        throw new AppConfigException(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
