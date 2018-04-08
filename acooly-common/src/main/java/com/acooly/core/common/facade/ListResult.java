/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-07 17:55 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/** @author qiubo@yiji.com */
@Getter
@Setter
public class ListResult<T> extends ResultBase implements DtoAble{
  private List<T> dto;

  public static <T> ListResult<T> from(List<T> list) {
    ListResult<T> result = new ListResult<>();
    result.setDto(list);
    result.setStatus(ResultStatus.success);
    return result;
  }

  /** 把T类型PageInfo转换为S类型PageInfo后构造结果对象 */
  public static <T, S> ListResult<S> from(List<T> list, Class<S> clazz) {
    ListResult<S> result = new ListResult<>();
    if (list != null && !list.isEmpty()) {
      List<S> sList = Lists.newArrayListWithCapacity(list.size());
      for (T t : list) {
        sList.add(BeanCopier.copy(t, clazz, BeanCopier.CopyStrategy.IGNORE_NULL));
      }
      result.setDto(sList);
    } else {
      result.setDto(Collections.emptyList());
    }
    result.setStatus(ResultStatus.success);
    return result;
  }
}
