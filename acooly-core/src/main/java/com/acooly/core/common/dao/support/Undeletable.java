package com.acooly.core.common.dao.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 逻辑删除 anno
 *
 * @author zhangpu
 * @date 2012年8月30日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Undeletable {
  /**
   * 伪删除的列
   *
   * @return String
   */
  String status() default "status";

  /**
   * 标志位
   *
   * @return String
   */
  String value() default "1";
}
