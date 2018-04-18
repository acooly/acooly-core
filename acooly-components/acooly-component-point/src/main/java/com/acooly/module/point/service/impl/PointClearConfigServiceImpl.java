/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 */
package com.acooly.module.point.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Dates;
import com.acooly.module.point.dao.PointClearConfigDao;
import com.acooly.module.point.domain.PointClearConfig;
import com.acooly.module.point.service.PointClearConfigService;
import com.acooly.module.point.service.PointTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 积分清零设置 Service实现
 * <p>
 * <p>
 * Date: 2017-04-19 16:24:31
 *
 * @author acooly
 */
@Service("pointClearConfigService")
public class PointClearConfigServiceImpl extends EntityServiceImpl<PointClearConfig, PointClearConfigDao>
        implements PointClearConfigService {

    @Autowired
    private PointTradeService pointTradeService;

    @Override
    public long getClearPoint(String userName, Date tradeTime) {
        String tradeTimeStr = Dates.format(tradeTime, Dates.CHINESE_DATETIME_FORMAT_LINE);
        PointClearConfig pointClearConfig = getEntityDao().getOneClearConfigByTradeTime(tradeTimeStr);
        if (pointClearConfig == null) {
            return 0l;
        }
        String startClearTime = Dates.format(pointClearConfig.getStartClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);
        String endClearTime = Dates.format(pointClearConfig.getEndClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);
        String clearTime = Dates.format(pointClearConfig.getClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);

        long producePoint = pointTradeService.getProducePoint(userName, startClearTime, endClearTime);
        long expensePoint = pointTradeService.getExpensePoint(userName, startClearTime, clearTime);
        long clearPoint = producePoint - expensePoint;
        return (clearPoint) > 0 ? clearPoint : 0l;
    }
}
