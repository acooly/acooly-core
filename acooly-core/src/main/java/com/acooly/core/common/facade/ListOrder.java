/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-07 17:54 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.Ids;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/** @author qiubo@yiji.com */
@Getter
@Setter
public class ListOrder<T> extends SingleOrder<T> {

  /** 参数map */
  private Map<String, Object> map;

  private Map<String, Boolean> sortMap;

  public static <T> ListOrder<T> from(T dto) {
    ListOrder<T> order = new ListOrder<T>();
    order.setDto(dto);
    return order;
  }

  public ListOrder<T> map(String key, Object value) {
    if (this.map == null) {
      this.map = Maps.newHashMap();
    }
    map.put(key, value);
    return this;
  }

  public ListOrder<T> sortMap(String key, Boolean value) {
    if (this.sortMap == null) {
      this.sortMap = Maps.newHashMap();
    }
    sortMap.put(key, value);
    return this;
  }

  public ListOrder<T> gid() {
    return this.gid(Ids.gid());
  }

  public ListOrder<T> gid(String gid) {
    this.setGid(gid);
    return this;
  }

  public ListOrder<T> partnerId(String partnerId) {
    this.setPartnerId(partnerId);
    return this;
  }
}
