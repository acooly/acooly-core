package com.acooly.module.lottery.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum WinnerStatus {
  winning("winning", "已抽奖"),

  awardPaying("awardPaying", "发奖支付中"),

  award("award", "已发奖");

  private String code;
  private String message;

  private WinnerStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (WinnerStatus type : values()) {
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
