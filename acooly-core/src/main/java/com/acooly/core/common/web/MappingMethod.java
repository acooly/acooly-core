/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年6月18日
 *
 */
package com.acooly.core.common.web;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangpu
 */
public enum MappingMethod implements Messageable {
    list("list", "分页"),

    query("query", "查询"),

    create("create", "新增"),

    show("show", "读取"),

    update("update", "更新"),

    delete("delete", "删除"),

    move("move", "移动"),

    exports("export", "导出"),

    imports("import", "导入");

    private final String code;
    private final String message;

    private MappingMethod(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (MappingMethod type : values()) {
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
    public static MappingMethod find(String code) {
        for (MappingMethod status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("MappingMethod not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<MappingMethod> getAll() {
        List<MappingMethod> list = new ArrayList<MappingMethod>();
        for (MappingMethod status : values()) {
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
        for (MappingMethod status : values()) {
            list.add(status.code());
        }
        return list;
    }

    public static boolean allow(String codes, MappingMethod mappingMethod) {
        if (Strings.contains(codes, "*")) {
            return true;
        }
        boolean allow = false;
        for (String code : Strings.split(codes, ",")) {
            allow = mappingMethod.code.equals(code);
            if (allow) {
                break;
            }
        }
        return allow;
    }

    public static boolean allow(String codes, String code) {
        return allow(codes, MappingMethod.find(code));
    }

    public static boolean canList(String codes) {
        return allow(codes, MappingMethod.list);
    }

    public static boolean canQuery(String codes) {
        return allow(codes, MappingMethod.query);
    }

    public static boolean canCreate(String codes) {
        return allow(codes, MappingMethod.create);
    }

    public static boolean canShow(String codes) {
        return allow(codes, MappingMethod.show);
    }

    public static boolean canUpdate(String codes) {
        return allow(codes, MappingMethod.update);
    }

    public static boolean canDelete(String codes) {
        return allow(codes, MappingMethod.delete);
    }

    public static boolean canImport(String codes) {
        return allow(codes, MappingMethod.imports);
    }

    public static boolean canExport(String codes) {
        return allow(codes, MappingMethod.exports);
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

    @Override
    public String toString() {
        return String.format("%s:%s", this.code, this.message);
    }
}
