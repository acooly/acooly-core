/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 11:56 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public enum  ResultCode implements Messageable {
    SUCCESS("EXECUTE_SUCCESS", "成功"),
    PROCESSING("EXECUTE_PROCESSING", "处理中"),
    INTERNAL_ERROR("INTERNAL_ERROR", "内部错误"),
    PARAMETER_ERROR("PARAMETER_ERROR", "参数错误"),
    FAILURE("FAILURE", "执行失败"),
    UN_AUTHENTICATED_ERROR("UNAUTHENTICATED", "认证(签名)错误"),
    PARAM_FORMAT_ERROR("PARAM_FORMAT_ERROR", "参数格式错误"),
    REQUEST_NO_NOT_UNIQUE("REQUEST_NO_NOT_UNIQUE", "请求号重复"),
    FIELD_NOT_UNIQUE("FIELD_NOT_UNIQUE", "对象字段重复"),
    REQUEST_GID_NOT_EXSIT("REQUEST_GID_NOT_EXSIT", "gid不存在");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static Map<String, String> mapping() {
        LinkedHashMap map = Maps.newLinkedHashMap();
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCode type = var1[var3];
            map.put(type.getCode(), type.getMessage());
        }

        return map;
    }

    public static ResultCode findStatus(String code) {
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCode status = var1[var3];
            if(status.getCode().equals(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException("ResultCode not legal:" + code);
    }

    public static List<ResultCode> getAllStatus() {
        ArrayList list = new ArrayList();
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCode status = var1[var3];
            list.add(status);
        }

        return list;
    }

    public static List<String> getAllStatusCode() {
        ArrayList list = new ArrayList();
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResultCode status = var1[var3];
            list.add(status.code());
        }

        return list;
    }

    public String toString() {
        return String.format("%s:%s", this.code, this.message);
    }
}
