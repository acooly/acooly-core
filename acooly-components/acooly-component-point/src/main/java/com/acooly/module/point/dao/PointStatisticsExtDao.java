package com.acooly.module.point.dao;

/**
 * 积分统计 Mybatis Dao
 *
 * <p>Date: 2017-03-13 11:51:10
 *
 * @author acooly
 */
public interface PointStatisticsExtDao {

  /**
   * 根据用户交易记录统计用户积分
   *
   * @param startTime
   * @param endTime
   */
  void pointStatistics(String startTime, String endTime);
}
