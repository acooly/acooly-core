/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年5月14日
 *
 */
package com.acooly.core.utils.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.PrivateKey;

/**
 * RSA Cipher 线程缓存
 *
 * @author zhangpu
 */
public class RSACipherCache {

  /** 每个线程可以缓存最多100个不同秘钥对Cipher */
  private static final int THREAD_CACHE_SIZE = 100;

  private static final ThreadLocal<LoadingCache<Key, Cipher>> threadLocal =
      new ThreadLocal<LoadingCache<Key, Cipher>>() {

        @Override
        protected LoadingCache<Key, Cipher> initialValue() {
          /** 设置一个按容量控制的内存缓存 */
          return CacheBuilder.newBuilder()
              .recordStats()
              .maximumSize(THREAD_CACHE_SIZE)
              .build(
                  new CacheLoader<Key, Cipher>() {
                    @Override
                    public Cipher load(final Key key) throws Exception {
                      Cipher cipher = null;
                      try {
                        // 这里暂不考虑算法的其他Provider,使用默认sunJCE
                        cipher = Cipher.getInstance(key.getAlgorithm());
                        if (PrivateKey.class.isAssignableFrom(key.getClass())) {
                          // 私钥，在加解密场景用于解密
                          cipher.init(Cipher.DECRYPT_MODE, key);
                        } else {
                          cipher.init(Cipher.ENCRYPT_MODE, key);
                        }
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                      return cipher;
                    }
                  });
        }
      };

  private RSACipherCache() {}

  public static Cipher newCipher(final Key key) {
    if (key == null) {
      throw new RuntimeException("Key must be not null.");
    }
    return threadLocal.get().getUnchecked(key);
  }

  public static CacheStats getStats() {
    return threadLocal.get().stats();
  }
}
