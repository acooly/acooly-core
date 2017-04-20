/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 */
package com.acooly.module.point.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Dates;
import com.acooly.module.point.dao.PointClearConfigDao;
import com.acooly.module.point.domain.PointClearConfig;
import com.acooly.module.point.domain.PointTrade;
import com.acooly.module.point.service.PointClearConfigService;
import com.acooly.module.point.service.PointTradeService;

/**
 * 积分清零设置 Service实现
 *
 * Date: 2017-04-19 16:24:31
 *
 * @author acooly
 *
 */
@Service("pointClearConfigService")
public class PointClearConfigServiceImpl extends EntityServiceImpl<PointClearConfig, PointClearConfigDao>
		implements PointClearConfigService {

	@Autowired
	private PointTradeService pointTradeService;

	@Override
	public long getClearPoint(String userName, Date tradeTime) {
		PointClearConfig pointClearConfig = null;
		String tradeTimeStr = Dates.format(tradeTime, Dates.CHINESE_DATETIME_FORMAT_LINE);
		List<PointClearConfig> lists = getEntityDao().getClearConfigByTradeTime(tradeTimeStr);
		if (!lists.isEmpty()) {
			pointClearConfig = lists.get(0);
			String startClearTime = Dates.format(pointClearConfig.getStartClearTime(),
					Dates.CHINESE_DATETIME_FORMAT_LINE);
			String endClearTime = Dates.format(pointClearConfig.getEndClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);
			return pointTradeService.getClearPoint(userName, startClearTime, endClearTime);
		}
		return 0l;
	}

}
