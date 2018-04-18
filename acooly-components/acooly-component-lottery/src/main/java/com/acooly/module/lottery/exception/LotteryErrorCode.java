/**
 * Copyright (c) 2016, acooly All Rights Reserved. create by zhangpu date:2016年3月15日
 */
package com.acooly.module.lottery.exception;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author zhangpu
 */
public enum LotteryErrorCode {
    VoteFail("VoteFail", "抽奖投票失败"),

    NotOpportunity("NotOpportunity", "您的参数次数已用完"),;

    private String code;
    private String message;

    private LotteryErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static LotteryErrorCode codeOf(String code) {
        for (LotteryErrorCode status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (LotteryErrorCode type : values()) {
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
