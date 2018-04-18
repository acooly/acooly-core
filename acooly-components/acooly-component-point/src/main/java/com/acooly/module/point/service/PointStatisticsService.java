/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-13
 *
 */
package com.acooly.module.point.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointStatistics;

/**
 * 积分统计 Service接口
 *
 * <p>Date: 2017-03-13 11:51:10
 *
 * @author acooly
 */
public interface PointStatisticsService extends EntityService<PointStatistics> {

    public void pointStatistics(String startTime, String endTime);
}
