/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-02-01 13:43
 */
package com.acooly.core.common.enums;
/**
 * 银行编码 枚举
 *
 * @author zhangpu
 * @date 2021-02-01 13:43
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum BankEnum implements Messageable {

    /***
     * 国有大银行
     */
    ICBC("ICBC", "工商银行"),
    ABC("ABC", "农业银行"),
    BOC("BOC", "中国银行"),
    CCB("CCB", "建设银行"),
    BCM("BCM", "交通银行"),
    PSBC("PSBC", "邮政银行"),


    /***
     * 全国性股份制商业银行
     */
    CMB("CMB", "招商银行"),
    CITIC("CITIC", "中信银行"),
    SPDB("SPDB", "浦发银行"),
    CIB("CIB", "兴业银行"),
    CMBC("CMBC", "民生银行"),
    CEB("CEB", "光大银行"),
    SPABANK("SPABANK", "平安银行"),
    HXB("HXB", "华夏银行"),
    CGB("CGB", "广发银行股份有限公司"),

    /**
     * 网络银行
     */


    /**
     * 民营银行
     */
    CQFMB("CQFMB", "重庆富民银行"),
    HSB("HSB", "徽商银行"),
    BSB("BSB", "包商银行"),
    /**
     * 外资银行
     */
    HANABAN("HANABAN", "韩亚银行"),
    SHBANK("SHBANK", "新韩银行"),
    EGB("EGB", "恒丰银行"),
    BEA("BEA", "东亚银行有限公司"),

    /**
     * 地方区域性银行
     */
    FDB("FDB", "富滇银行"),
    HKB("HKB", "汉口银行"),
    CQCB("CQCB", "重庆银行"),
    BOCD("BOCD", "成都商业银行"),
    NJCB("NJCB", "南京银行"),
    NBCB("NBCB", "宁波银行"),
    BOB("BOB", "北京银行"),
    BOS("BOS", "上海银行"),
    JSB("JSB", "江苏银行"),
    DYCCB("DYCCB", "东营银行"),
    CQTGB("CQTGB", "重庆三峡银行"),
    BOZZ("BOZZ", "枣庄银行"),
    YTCB("YTCB", "烟台银行"),
    BOBD("BOBD", "保定银行"),
    NXXN("NXXN", "宁夏银行"),


    /**
     * 其他
     */
    CUP("CUP", "中国银联支付标记"),

    ;


    private final String code;
    private final String message;

    BankEnum(String code, String message) {
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
        for (BankEnum type : values()) {
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
    public static BankEnum find(String code) {
        for (BankEnum status : values()) {
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
    public static List<BankEnum> getAll() {
        List<BankEnum> list = new ArrayList<BankEnum>();
        for (BankEnum status : values()) {
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
        for (BankEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
