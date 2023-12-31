/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 10:51 创建
 */
package com.acooly.core.common.exception;

import com.acooly.core.common.facade.ResultCode;
import com.acooly.core.utils.enums.Messageable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
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
     * @param msg       校验失败提示信息
     */
    public static void throwIt(String parameter, String msg) {
        throw new OrderCheckException(parameter, msg);
    }

    /**
     * 抛出OrderCheckException
     * @param parameter 校验失败参数
     * @param format 格式
     * @param params 参数
     */
    public static void throwIt(String parameter, String format,Object... params) {
        throw new OrderCheckException(parameter, String.format(format,params));
    }

    /**
     * 当布尔表达式为false时，抛出OrderCheckException
     *
     * @param expression boolean表达式
     * @param parameter  校验失败参数
     * @param msg        校验失败提示信息
     */
    public static void throwIfStateFalse(boolean expression, String parameter, String msg) {
        if (!expression) {
            throwIt(parameter, msg);
        }
    }
    /**
     * 当布尔表达式为true时，抛出OrderCheckException
     *
     * @param expression boolean表达式
     * @param parameter  校验失败参数
     * @param msg        校验失败提示信息
     */
    public static void throwIf(boolean expression, String parameter, String msg) {
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
     * @param msg       参数信息
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
                    String key = nomalize(entry);
                    sb.append(key).append(":").append(entry.getValue()).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                msg = sb.toString();
            }
        }
        return msg;
    }

    private String nomalize(Map.Entry entry) {
        String key = entry.getKey().toString();
        int index = key.indexOf("dto.");
        if (index > -1) {
            key = key.substring(index + 4, key.length());
        }
        return key;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String code() {
        return ResultCode.PARAMETER_ERROR.getCode();
    }

    @Override
    public String message() {
        return this.getMessage();
    }
}
