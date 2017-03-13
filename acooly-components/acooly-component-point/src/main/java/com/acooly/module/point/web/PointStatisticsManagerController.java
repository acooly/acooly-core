/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-03-13
*/
package com.acooly.module.point.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.point.domain.PointStatistics;
import com.acooly.module.point.enums.PointStaticsStatus;
import com.acooly.module.point.service.PointStatisticsService;

/**
 * 积分统计 管理控制器
 * 
 * @author acooly
 * Date: 2017-03-13 11:51:10
 */
@Controller
@RequestMapping(value = "/manage/point/pointStatistics")
public class PointStatisticsManagerController extends AbstractJQueryEntityController<PointStatistics, PointStatisticsService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private PointStatisticsService pointStatisticsService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allStatuss", PointStaticsStatus.mapping());
	}

}
