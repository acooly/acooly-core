/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-11-05 07:09 创建
 */
package com.acooly.module.cache.declarative;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;

/** @author qiubo */
@Slf4j
public class DefaultCacheErrorHandler extends SimpleCacheErrorHandler {
  @Override
  public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
    log.warn("get cache error,key={}", key, exception);
  }

  @Override
  public void handleCachePutError(
      RuntimeException exception, Cache cache, Object key, Object value) {
    log.warn("put cache error,key={}", key, exception);
  }

  @Override
  public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
    log.warn("evict cache error,key={}", key, exception);
  }

  @Override
  public void handleCacheClearError(RuntimeException exception, Cache cache) {
    log.warn("clear cache error", exception);
  }
}
