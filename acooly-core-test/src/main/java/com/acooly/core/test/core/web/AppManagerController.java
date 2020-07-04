/*
* acooly.cn Inc.
* Copyright (c) 2020 All Rights Reserved.
* create by acooly
* date:2020-07-03
*/
package com.acooly.core.test.core.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.core.service.AppService;

import com.google.common.collect.Maps;

/**
 * app 管理控制器
 * 
 * @author acooly
 * @date 2020-07-03 16:28:38
 */
@Controller
@RequestMapping(value = "/manage/test/core/app")
public class AppManagerController extends AbstractJsonEntityController<App, AppService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private AppService appService;

	

}
