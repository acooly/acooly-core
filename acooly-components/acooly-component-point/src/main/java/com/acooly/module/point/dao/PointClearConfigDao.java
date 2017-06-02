/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 */
package com.acooly.module.point.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.point.domain.PointClearConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 积分清零设置 Mybatis Dao
 *
 * <p>Date: 2017-04-19 16:24:31
 *
 * @author acooly
 */
public interface PointClearConfigDao extends EntityMybatisDao<PointClearConfig> {

  @Select(
      "select * from point_clear_config where start_trade_time<=#{tradeTimeStr} and end_trade_time>= #{tradeTimeStr}")
  List<PointClearConfig> getClearConfigByTradeTime(@Param("tradeTimeStr") String tradeTimeStr);
}
