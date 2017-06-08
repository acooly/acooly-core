package com.acooly.core.utils.id;

/**
 * id生成器
 *
 * @author qiubo
 */
public interface IdentifierGenerator<T> {
  /**
   * 生成长度19位的全局唯一的、全为数字的id
   *
   * @return
   */
  T generate();
}
