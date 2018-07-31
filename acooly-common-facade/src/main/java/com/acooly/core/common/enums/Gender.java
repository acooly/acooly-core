package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-31 13:38
 */
public enum Gender implements Messageable {
    MALE("M", "男"), FEMALE("F", "女"),UNKOWN("U","未知");
    private final String code;
    private final String message;

    Gender(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Gender{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
