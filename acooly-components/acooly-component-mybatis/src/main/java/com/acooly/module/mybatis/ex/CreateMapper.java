/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:18 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

/** @author qiubo@yiji.com */
public interface CreateMapper<T> {
  @InsertProvider(type = CreateProvider.class, method = "dynamicSQL")
  void create(T o);

  class CreateProvider extends BaseInsertProvider {
    public CreateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
      super(mapperClass, mapperHelper);
    }

    public String create(MappedStatement ms) {
      return super.insert(ms);
    }
  }
}
