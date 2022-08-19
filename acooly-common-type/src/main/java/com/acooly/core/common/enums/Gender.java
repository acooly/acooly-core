package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @zhangpu 加入其他helper方法，兼容汉字性别 2018-07-31
 * @date 2018-07-31 13:38
 */
public enum Gender implements Messageable {
    male("male", "男"),
    female("female", "女");

    private final String code;
    private final String message;

    private Gender(String code, String message) {
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
        Map<String, String> map = new LinkedHashMap<>();
        for (Gender type : values()) {
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
    public static Gender find(String code) {
        for (Gender status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }

        if (Gender.male.message.equals(code)) {
            return Gender.male;
        }
        if (Gender.female.message.equals(code)) {
            return Gender.female;
        }

        throw null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<Gender> getAll() {
        List<Gender> list = new ArrayList<Gender>();
        for (Gender status : values()) {
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
        for (Gender status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
