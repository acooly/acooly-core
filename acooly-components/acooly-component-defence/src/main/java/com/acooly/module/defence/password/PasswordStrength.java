/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 19:16
 */
package com.acooly.module.defence.password;
/**
 * @author zhangpu
 * @date 2019-11-23 19:16
 */

import com.acooly.core.utils.Regexs;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.module.defence.exception.DefenceErrorCodes;
import com.acooly.module.defence.exception.DefenceException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum PasswordStrength implements Messageable {

    /**
     * 简单强度:密码由任意字母、数字、下划线组成，长度6-16字节(一般用于开发测试阶段)
     */
    simple("simple", "简单强度", "密码由任意字母、数字、下划线组成，长度6-16字节", "[\\w]{6,16}"),

    /**
     * 常用强度:密码必须由大小写字母、数字3种字符共同组成，长度8-16字节(一般用于中低安全级别系统线上环境)
     */
    usually("usually", "常用强度", "密码必须由大小写字母、数字3种字符共同组成，长度8-16字节",
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,16}$"),

    /**
     * 复杂强度:密码必须由大小写字母、数字和特殊字符4种字符共同组成，长度8-16字节(一般用于高安全级别系统线上环境)
     */
    complex("complex", "复杂强度", "密码必须由大小写字母、数字和特殊字符4种字符共同组成，长度8-16字节",
            "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,16}");

    private final String code;
    private final String message;
    private final String detail;
    private final String regex;

    PasswordStrength(String code, String message, String detail, String regex) {
        this.code = code;
        this.detail = detail;
        this.message = message;
        this.regex = regex;
    }


    /**
     * 验证密码强度
     *
     * @param password
     */
    public void verify(String password) {
        if (!Regexs.matcher(this.regex, password)) {
            throw new DefenceException(DefenceErrorCodes.PASSWORD_STRENGTH_VERIFY_ERROR, this.detail);
        }
    }

    public static PasswordStrength determine(String password) {
        if (Regexs.matcher(complex.regex, password)) {
            return complex;
        }
        if (Regexs.matcher(usually.regex, password)) {
            return usually;
        }
        return simple;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public String getRegex() {
        return regex;
    }

    public String getRegexForJs() {
        return Strings.replace(this.getRegex(), "\\", "\\\\");
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
        for (PasswordStrength type : values()) {
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
    public static PasswordStrength find(String code) {
        for (PasswordStrength status : values()) {
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
    public static List<PasswordStrength> getAll() {
        List<PasswordStrength> list = new ArrayList<PasswordStrength>();
        for (PasswordStrength status : values()) {
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
        for (PasswordStrength status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
