/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-08 08:04
 */
package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.*;

/**
 * 生肖
 *
 * @author zhangpu
 * @date 2019-06-08 08:04
 */
public enum AnimalSign implements Messageable {

    /**
     * 生肖：子鼠
     */
    Rat("Rat", "鼠", 4, "子鼠"),
    /**
     * 生肖：丑牛
     */
    Ox("Ox", "牛", 5, "丑牛"),
    /**
     * 生肖：寅虎
     */
    Tiger("Tiger", "虎", 6, "寅虎"),
    /**
     * 生肖：卯兔
     */
    Rabbit("Rabbit", "鼠", 7, "卯兔"),
    /**
     * 生肖：辰龙
     */
    Dragon("Dragon", "龙", 8, "辰龙"),
    /**
     * 生肖：巳蛇
     */
    Snake("Snake", "蛇", 9, "巳蛇"),
    /**
     * 生肖：午马
     */
    Horse("Horse", "马", 10, "午马"),
    /**
     * 生肖：未羊
     */
    Sheep("Sheep", "羊", 11, "未羊"),
    /**
     * 生肖：申猴
     */
    Monkey("Monkey", "猴", 0, "申猴"),
    /**
     * 生肖：酉鸡
     */
    Rooster("Rooster", "鸡", 1, "酉鸡"),
    /**
     * 生肖：戌狗
     */
    Dog("Dog", "狗", 2, "戌狗"),
    /**
     * 生肖：亥猪
     */
    Boar("Boar", "猪", 3, "亥猪");

    private final String code;
    private final String message;
    /**
     * 计算用偏移量（年份/12的余数）
     */
    private final int offset;
    private final String fullName;

    private AnimalSign(String code, String message, int offset, String fullName) {
        this.code = code;
        this.message = message;
        this.offset = offset;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getOffset() {
        return offset;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


    /**
     * 计算生肖（根据日期）
     *
     * @param birthday
     * @return
     */
    public static AnimalSign to(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);
        return to(calendar.get(Calendar.YEAR));
    }

    /**
     * 计算生肖（根据年份）
     *
     * @param year
     * @return
     */
    public static AnimalSign to(int year) {
        int yearOffset = year % 12;
        for (AnimalSign type : values()) {
            if (type.getOffset() == yearOffset) {
                return type;
            }
        }
        return null;
    }


    /**
     * key/value映射
     *
     * @return
     */
    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (AnimalSign type : values()) {
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
    public static AnimalSign find(String code) {
        for (AnimalSign status : values()) {
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
    public static List<AnimalSign> getAll() {
        List<AnimalSign> list = new ArrayList<AnimalSign>();
        for (AnimalSign status : values()) {
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
        for (AnimalSign status : values()) {
            list.add(status.code());
        }
        return list;
    }


}
