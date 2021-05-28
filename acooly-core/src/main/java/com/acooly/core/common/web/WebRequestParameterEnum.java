/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-04-02 02:54 创建
 */
package com.acooly.core.common.web;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author acooly
 */
public enum WebRequestParameterEnum implements Messageable {
    importIgnoreTitle("importIgnoreTitle", "忽略导入文件第一行", "true:忽略,false:不忽略(默认)"),

    importFileType("importFileType", "导入文件类型", "CSV,EXCEL"),

    exportFileName("exportFileName", "导出文件名称", "默认为实体名称"),

    exportCharset("exportCharset", "导出文件编码", "默认UTF-8");

    private final String code;
    private final String message;
    private final String comments;

    private WebRequestParameterEnum(String code, String message, String comments) {
        this.code = code;
        this.message = message;
        this.comments = comments;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (WebRequestParameterEnum type : values()) {
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
    public static WebRequestParameterEnum find(String code) {
        for (WebRequestParameterEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("WebRequestParameterEnum not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<WebRequestParameterEnum> getAll() {
        List<WebRequestParameterEnum> list = new ArrayList<WebRequestParameterEnum>();
        for (WebRequestParameterEnum status : values()) {
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
        for (WebRequestParameterEnum status : values()) {
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

    public String getComments() {
        return comments;
    }
}
