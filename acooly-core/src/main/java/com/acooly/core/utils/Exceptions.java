/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 *
 * @author calvin
 */
public class Exceptions {

  /** 将CheckedException转换为UncheckedException. */
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

  /** 将ErrorStack转化为String. */
  public static String getStackTraceAsString(Exception e) {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  /** 判断异常是否由某些底层的异常引起. */
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
