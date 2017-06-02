/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 10:51 创建
 */
package com.acooly.core.common.exception;

import com.acooly.core.utils.enums.Messageable;

import java.util.HashMap;
import java.util.Map;

/** @author qiubo@yiji.com */
public class OrderCheckException extends IllegalArgumentException implements Messageable {

  private static final long serialVersionUID = 1L;

  private Map<String, String> errorMap = new HashMap<>();

  private String msg;

  public OrderCheckException() {
    super();
  }

  public OrderCheckException(String parameter, String msg) {
    super();
    this.addError(parameter, msg);
  }

  public OrderCheckException(Throwable cause) {
    super(cause);
  }

  /**
   * 抛出OrderCheckException
   *
   * @param parameter 校验失败参数
   * @param msg 校验失败提示信息
   */
  public static void throwIt(String parameter, String msg) {
    throw new OrderCheckException(parameter, msg);
  }

  /**
   * 当布尔表达式为false时，抛出OrderCheckException
   *
   * @param expression boolean表达式
   * @param parameter 校验失败参数
   * @param msg 校验失败提示信息
   */
  public static void throwIfStateFalse(boolean expression, String parameter, String msg) {
    if (!expression) {
      throwIt(parameter, msg);
    }
  }

  public Map<String, String> getErrorMap() {
    return errorMap;
  }

  /**
   * 增加参数错误信息
   *
   * @param parameter 校验失败参数
   * @param msg 参数信息
   */
  public void addError(String parameter, String msg) {
    this.errorMap.put(parameter, msg);
    this.msg = null;
  }

  @Override
  public String getMessage() {
    if (msg == null) {
      if (errorMap.isEmpty()) {
        msg = "";
      } else {
        StringBuilder sb = new StringBuilder(errorMap.size() * 15);
        for (Map.Entry entry : errorMap.entrySet()) {
          sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        msg = sb.toString();
      }
    }
    return msg;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

  @Override
  public String code() {
    return "INVALID_ARGUMENTS";
  }

  @Override
  public String message() {
    return this.getMessage();
  }
}
