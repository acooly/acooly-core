/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-18 17:46 创建
 */
package com.acooly.module.filterchain;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 过滤器链
 *
 * @author qiubo@yiji.com
 */
@ThreadSafe
public interface FilterChain<C extends Context> {
  /**
   * 执行过滤器链
   *
   * @param context 上下文对象
   */
  void doFilter(C context);
}
