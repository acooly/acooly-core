package com.acooly.module.lottery.enums;

import com.google.common.collect.Maps;

import java.util.Map;

public enum LotteryAwardType {
  goods("goods", "实物"),

  money("money", "现金"),

  virtual("virtual", "虚拟货品");

  private String code;
  private String message;

  private LotteryAwardType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static LotteryAwardType codeOf(String code) {
    for (LotteryAwardType _enum : values()) {
      if (_enum.getCode().equals(code)) {
        return _enum;
      }
    }
    return null;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (LotteryAwardType type : values()) {
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
