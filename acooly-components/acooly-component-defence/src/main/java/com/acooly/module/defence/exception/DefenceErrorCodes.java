/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 17:41
 */
package com.acooly.module.defence.exception;

/**
 * 防御错误码定义
 *
 * @author zhangpu
 * @date 2019-11-23 17:41
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum DefenceErrorCodes implements Messageable {

    HHA_FORBIDDEN("HHA_FORBIDDEN", "Http头攻击防御"),


    PASSWORD_STRENGTH_VERIFY_ERROR("PASSWORD_STRENGTH_VERIFY_ERROR", "密码强度验证未通过"),

    ;

    private final String code;
    private final String message;

    DefenceErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (DefenceErrorCodes type : values()) {
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
    public static DefenceErrorCodes find(String code) {
        for (DefenceErrorCodes status : values()) {
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
    public static List<DefenceErrorCodes> getAll() {
        List<DefenceErrorCodes> list = new ArrayList<DefenceErrorCodes>();
        for (DefenceErrorCodes status : values()) {
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
        for (DefenceErrorCodes status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
