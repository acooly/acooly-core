/** create by zhangpu date:2015年11月13日 */
package com.acooly.core.common.enums;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 代替 @See com.acooly.core.utils.enums.SimpleStatus
 *
 * @author zhangpu
 * @date 2015年11月13日
 */
@Deprecated
public enum EntityStatus implements ResultEnum {
  enable("enable", "可用"),

  disable("disable", "禁用");

  private final String code;
  private final String message;

  private EntityStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (EntityStatus type : values()) {
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
  public static EntityStatus findStatus(String code) {
    for (EntityStatus status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("EntityStatus not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<EntityStatus> getAllStatus() {
    List<EntityStatus> list = new ArrayList<EntityStatus>();
    for (EntityStatus status : values()) {
      list.add(status);
    }
    return list;
  }

  /**
   * 获取全部枚举值码。
   *
   * @return 全部枚举值码。
   */
  public static List<String> getAllStatusCode() {
    List<String> list = new ArrayList<String>();
    for (EntityStatus status : values()) {
      list.add(status.code());
    }
    return list;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String code() {
    return code;
  }

  public String message() {
    return message;
  }

  @Override
  public String toString() {
    return String.format("%s:%s", this.code, this.message);
  }
}
