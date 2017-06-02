package com.acooly.module.lottery.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum LotteryStatus {
  enable("enable", "正常"),

  pause("pause", "暂停"),

  disable("disable", "完结");

  private String code;
  private String message;

  private LotteryStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static LotteryStatus codeOf(String code) {
    for (LotteryStatus status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    return null;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (LotteryStatus type : values()) {
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
