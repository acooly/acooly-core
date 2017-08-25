/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:16 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;

/** @author qiubo@yiji.com */
public class SingleResult<T> extends ResultBase {
  private T dto;

  public static <T> SingleResult<T> from(T dto) {
    SingleResult<T> singleResult = new SingleResult<>();
    singleResult.setDto(dto);
    singleResult.setStatus(ResultStatus.success);
    return singleResult;
  }
    public static <T> SingleResult<T> fromProcessing(T dto) {
        SingleResult<T> singleResult = new SingleResult<>();
        singleResult.setDto(dto);
        singleResult.markProcessing();
        return singleResult;
    }
  /** 把T类型转换为S类型后构造结果对象 */
  public static <T, S> SingleResult<S> from(T t, Class<S> clazz) {
    SingleResult<S> singleResult = new SingleResult<>();
    if (t != null) {
      singleResult.setDto(BeanCopier.copy(t, clazz, BeanCopier.CopyStrategy.IGNORE_NULL));
    }
    singleResult.setStatus(ResultStatus.success);
    return singleResult;
  }

  public T getDto() {
    return dto;
  }

  public void setDto(T dto) {
    this.dto = dto;
  }
}
