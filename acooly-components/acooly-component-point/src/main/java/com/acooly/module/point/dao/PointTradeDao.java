/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.point.domain.PointTrade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 积分交易信息 Mybatis Dao
 *
 * <p>Date: 2017-02-03 22:50:14
 *
 * @author cuifuqiang
 */
public interface PointTradeDao extends EntityMybatisDao<PointTrade> {

  @Select(
      "select coalesce(sum(case trade_type when 'produce' then amount else 0 end)-(case trade_type when 'expense' then amount else 0 end) ,0) as point "
          + "from point_trade where user_name=#{userName} " //
          + "and create_time>=#{startTime} and create_time<=#{endTime} " //
          + "and trade_type in('produce','expense')")
  long getClearPoint(
      @Param("userName") String userName,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);
}
