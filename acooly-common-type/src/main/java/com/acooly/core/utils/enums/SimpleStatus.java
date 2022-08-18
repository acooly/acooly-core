/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单状态
 *
 * @author zhangpu
 */
public enum SimpleStatus implements Messageable {
    enable("enable", "正常"),

    pause("pause", "暂停"),

    disable("disable", "禁用");

    private final String code;
    private final String message;

    SimpleStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<>();
        for (SimpleStatus type : values()) {
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
    public static SimpleStatus findStatus(String code) {
        for (SimpleStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("SimpleStatus not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<SimpleStatus> getAllStatus() {
        List<SimpleStatus> list = new ArrayList<SimpleStatus>();
        for (SimpleStatus status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllStatusCode() {
        List<String> list = new ArrayList<String>();
        for (SimpleStatus status : values()) {
            list.add(status.code());
        }
        return list;
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
}
