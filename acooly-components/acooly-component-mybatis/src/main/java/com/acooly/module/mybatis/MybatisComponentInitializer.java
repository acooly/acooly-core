/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-10 11:23 创建
 *
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.google.common.collect.Lists;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/** @author qiubo@yiji.com */
public class MybatisComponentInitializer implements ComponentInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    setPropertyIfMissing("mapper.mappers", "com.acooly.module.mybatis.EntityMybatisDao");
  }

  @Override
  public List<String> excludeAutoconfigClassNames() {
    Boolean supportMultiDataSource =
        EnvironmentHolder.get()
            .getProperty("acooly.mybatis.supportMultiDataSource", Boolean.class, Boolean.FALSE);
    if (supportMultiDataSource) {
      setPropertyIfMissing("acooly.ds.enable", "false");
      return Lists.newArrayList(
          "com.github.pagehelper.autoconfigure.MapperAutoConfiguration",
          "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration",
          "com.acooly.module.mybatis.MybatisAutoConfig");
    } else {
      return Lists.newArrayList(
          "com.github.pagehelper.autoconfigure.MapperAutoConfiguration",
          "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration");
    }
  }
}
