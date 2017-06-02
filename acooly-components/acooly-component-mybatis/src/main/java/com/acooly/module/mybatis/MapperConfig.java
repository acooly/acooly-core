/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-22 17:06 创建
 */
package com.acooly.module.mybatis;

import com.github.pagehelper.autoconfigure.MapperProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.annotation.PostConstruct;
import java.util.List;

/** @author qiubo@yiji.com */
@Configuration
@EnableConfigurationProperties({MapperProperties.class})
public class MapperConfig {
  private final List<SqlSessionFactory> sqlSessionFactoryList;
  @Autowired private MapperProperties mapperProperties;
  @Autowired private ApplicationContext applicationContext;

  public MapperConfig(List<SqlSessionFactory> sqlSessionFactoryList) {
    this.sqlSessionFactoryList = sqlSessionFactoryList;
  }

  @PostConstruct
  public void addPageInterceptor() {
    MapperHelper mapperHelper = new MapperHelper();
    mapperHelper.setConfig(mapperProperties);
    if (mapperProperties.getMappers().size() > 0) {
      for (Class mapper : mapperProperties.getMappers()) {
        applicationContext.getBeansOfType(mapper);
        mapperHelper.registerMapper(mapper);
      }
    } else {
      applicationContext.getBeansOfType(Mapper.class);
      mapperHelper.registerMapper(Mapper.class);
    }
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
    }
  }
}
