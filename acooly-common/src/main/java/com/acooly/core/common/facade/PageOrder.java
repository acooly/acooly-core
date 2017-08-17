/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-07 17:54 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Ids;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

/** @author qiubo@yiji.com */
@Getter
@Setter
public class PageOrder extends OrderBase {

  @NotNull private PageInfo pageInfo;
  /** 参数map */
  private Map<String, Object> map;

  private Map<String, Boolean> sortMap;

  public static  PageOrder from() {
    PageOrder order = new PageOrder();
    return order;
  }

  public PageOrder pageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
    return this;
  }

  public PageOrder pageInfo() {
    this.pageInfo = new PageInfo<>();
    return this;
  }

  public PageOrder map(String key, Object value) {
    if (this.map == null) {
      this.map = Maps.newHashMap();
    }
    map.put(key, value);
    return this;
  }

  public PageOrder sortMap(String key, Boolean value) {
    if (this.sortMap == null) {
      this.sortMap = Maps.newHashMap();
    }
    sortMap.put(key, value);
    return this;
  }

  public PageOrder gid() {
    return this.gid(Ids.gid());
  }

  public PageOrder gid(String gid) {
    this.setGid(gid);
    return this;
  }

  public PageOrder partnerId(String partnerId) {
    this.setPartnerId(partnerId);
    return this;
  }
}
