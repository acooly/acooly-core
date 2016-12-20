/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-12-19
 *
 */
package com.acooly.core.test.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.test.domain.App;
import com.acooly.core.test.service.AppService;

import com.google.common.collect.Maps;

/**
 * app 管理控制器
 * 
 * @author acooly
 * Date: 2016-12-19 21:09:09
 */
@Controller
@RequestMapping(value = "/manage/module/app/app")
public class AppManagerController extends AbstractJQueryEntityController<App, AppService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private AppService appService;

	

}
