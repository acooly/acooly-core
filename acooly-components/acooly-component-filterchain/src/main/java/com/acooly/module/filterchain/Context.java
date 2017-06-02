/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-18 18:13 创建
 */
package com.acooly.module.filterchain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 上下文对象
 *
 * @author qiubo@yiji.com
 */
public abstract class Context extends HashMap<String, Object> implements Serializable {
  protected transient Iterator<Filter<? extends Context>> iterator;

  /** 设置此对象可以重入filterChain */
  public void reentry() {
    this.iterator = null;
  }
}
