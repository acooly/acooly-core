/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-13
 */
package com.acooly.module.point.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.point.dao.PointStatisticsDao;
import com.acooly.module.point.dao.PointStatisticsExtDao;
import com.acooly.module.point.domain.PointStatistics;
import com.acooly.module.point.service.PointStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 积分统计 Service实现
 *
 * <p>Date: 2017-03-13 11:51:11
 *
 * @author acooly
 */
@Service("pointStatisticsService")
public class PointStatisticsServiceImpl
        extends EntityServiceImpl<PointStatistics, PointStatisticsDao>
        implements PointStatisticsService {

    @Autowired
    private PointStatisticsExtDao pointStatisticsExtDao;

    /**
     * 格式化时间
     */
    public void pointStatistics(String startTime, String endTime) {
        pointStatisticsExtDao.pointStatistics(startTime, endTime);
    }
}
