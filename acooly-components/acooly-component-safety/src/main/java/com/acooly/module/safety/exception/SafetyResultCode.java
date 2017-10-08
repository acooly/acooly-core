/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-10-07 16:14 创建
 */
package com.acooly.module.safety.exception;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangpu 2017-10-07 16:14
 */
public enum SafetyResultCode implements Messageable {


    SIGN_TYPE_UNSUPPORTED("SIGN_TYPE_UNSUPPORTED", "签名类型不支持"),

    LOAD_KEY_ERROR("LOAD_KEY_ERROR", "秘钥加载失败"),

    SIGN_CALC_ERROR("SIGN_CALC_ERROR", "签名失败"),

    SIGN_VERIFY_ERROR("SIGN_VERIFY_ERROR", "验证签名未通过");


    private final String code;
    private final String message;

    private SafetyResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (SafetyResultCode type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static SafetyResultCode find(String code) {
        for (SafetyResultCode status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<SafetyResultCode> getAll() {
        List<SafetyResultCode> list = new ArrayList<SafetyResultCode>();
        for (SafetyResultCode status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (SafetyResultCode status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
