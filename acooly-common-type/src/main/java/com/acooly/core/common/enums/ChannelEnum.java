/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhangpu@acooly.cn
 * date:2019-01-10
 *
 */
package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用渠道定义
 *
 * @author zhangpu@acooly.cn
 * Date: 2019-01-10 18:10:55
 */
public enum ChannelEnum implements Messageable {

    WECHAT("WECHAT", "微信"),

    H5("H5", "H5"),

    WEB("WEB", "网站"),

    ANDROID("ANDROID", "安卓"),

    IOS("IOS", "苹果"),

    OTHER("other", "其他");

    private final String code;
    private final String message;

    private ChannelEnum(String code, String message) {
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
        for (ChannelEnum type : values()) {
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
    public static ChannelEnum find(String code) {
        for (ChannelEnum status : values()) {
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
    public static List<ChannelEnum> getAll() {
        List<ChannelEnum> list = new ArrayList<ChannelEnum>();
        for (ChannelEnum status : values()) {
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
        for (ChannelEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
