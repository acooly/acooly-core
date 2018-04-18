package com.acooly.module.lottery.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum LotteryWhitelistStatus {
    apply("apply", "待抽奖"),

    failure("failure", "抽奖失败"),

    success("success", "抽奖成功");

    private String code;
    private String message;

    private LotteryWhitelistStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static LotteryWhitelistStatus codeOf(String code) {
        for (LotteryWhitelistStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (LotteryWhitelistStatus type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
