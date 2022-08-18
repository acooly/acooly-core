/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.enums.Messageable;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 *
 * @author zhangpu
 */
public class Exceptions {

    public static void rethrow(Messageable messageable, Throwable cause) {
        throw new BusinessException(messageable, cause);
    }

    public static void rethrow(Messageable messageable) {
        throw new BusinessException(messageable);
    }

    public static void rethrow(String code, String message) {
        throw new BusinessException(code, message, "");
    }

    public static void rethrow(String code, String message, Throwable throwable) {
        throw new BusinessException(code, message, throwable);
    }

    public static void rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR, throwable.getMessage());
        }
    }

    public static BusinessException rethrowBusinessException(Throwable throwable) {
        if (throwable instanceof BusinessException) {
            throw (BusinessException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR, throwable.getMessage());
        }
    }

    public static BusinessException rethrowBusinessException(Throwable throwable, String msg) {
        if (throwable instanceof BusinessException) {
            throw (BusinessException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR.code(), msg, throwable.getMessage());
        }
    }

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    public static RuntimeException runtimeException(String msg, Exception e) {
        return new RuntimeException(msg, e);
    }

    public static RuntimeException runtimeException(Exception e) {
        return unchecked(e);
    }

    public static RuntimeException runtimeException(String msg) {
        return new RuntimeException(msg);
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(
            Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}
