/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by cuifuqiang
* date:2017-02-03
*/
package com.acooly.module.point.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.point.domain.PointTrade;
import com.acooly.module.point.enums.PointTradeType;
import com.acooly.module.point.service.PointTradeService;

/**
 * 积分交易信息 管理控制器
 * 
 * @author cuifuqiang Date: 2017-02-03 22:50:14
 */
@Controller
@RequestMapping(value = "/manage/point/pointTrade")
public class PointTradeManagerController extends AbstractJQueryEntityController<PointTrade, PointTradeService> {

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private PointTradeService pointTradeService;

	@RequestMapping(value = "point")
	public String point(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			String userName = "cuifuq7";
			long point = 1000l;
			pointTradeService.pointProduce(userName, point, "");
			pointTradeService.pointExpense(userName, point, false, "");

		} catch (Exception e) {
			handleException("主界面", e, request);
		}
		return getListView();
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allTradeTypes", PointTradeType.mapping());
	}

}
