/** create by zhangpu date:2015年11月4日 */
package com.acooly.module.app.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 系统消息类型
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
public enum AppMessageType {
  broadcast("broadcast", "广播"),

  group("group", "群发");

  private String code;
  private String message;

  private AppMessageType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (AppMessageType type : values()) {
      map.put(type.getCode(), type.getMessage());
    }
    return map;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return this.code + " : " + this.message;
  }
}
