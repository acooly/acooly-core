package com.acooly.core.common.exception;

/**
 * 系统公告错误
 * <p>
 * 用于：业务层开发时，结合BusinessException进行错误处理，以便于上次facade和openapi统一错误模型
 *
 * @author zhangpu
 * @date 2018-10-17 16:55
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum CommonErrorCodes implements Messageable {


    INTERNAL_ERROR("INTERNAL_ERROR", "系统错误"),

    OBJECT_NOT_EXIST("OBJECT_NOT_EXIST", "对象不存在"),

    OBJECT_NOT_UNIQUE("OBJECT_NOT_UNIQUE", "对象不存在"),

    PARAMETER_ERROR("PARAMETER_ERROR", "参数错误");
    private final String code;
    private final String message;

    CommonErrorCodes(String code, String message) {
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
        for (CommonErrorCodes type : values()) {
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
    public static CommonErrorCodes find(String code) {
        for (CommonErrorCodes status : values()) {
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
    public static List<CommonErrorCodes> getAll() {
        List<CommonErrorCodes> list = new ArrayList<CommonErrorCodes>();
        for (CommonErrorCodes status : values()) {
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
        for (CommonErrorCodes status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
