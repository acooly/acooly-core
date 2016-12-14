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
 * @author qiubo@yiji.com
 */
public class AppConfigException extends RuntimeException {
    public AppConfigException(Throwable cause) {
        super(cause);
    }

    public AppConfigException(String message) {
        super(message);
    }

    public AppConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
