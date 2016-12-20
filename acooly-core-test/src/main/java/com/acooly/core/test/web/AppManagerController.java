/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-12-19
 *
 */
package com.acooly.core.test.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.test.domain.App;
import com.acooly.core.test.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * app 管理控制器
 * 
 * @author acooly Date: 2016-12-19 21:09:09
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
	
	@RequestMapping("/testFtl")
	public String testFtl(ModelMap modelMap) {
		modelMap.put("name", "na");
		modelMap.put("message", "hi");
		return "test";
	}
	
}
