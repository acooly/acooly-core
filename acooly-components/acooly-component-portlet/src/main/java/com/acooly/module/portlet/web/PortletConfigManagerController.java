/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-03-01
*/
package com.acooly.module.portlet.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.portlet.entity.PortletConfig;
import com.acooly.module.portlet.service.PortletConfigService;
import com.acooly.module.portlet.enums.PortletConfigTypeEnum;

/**
 * p_portlet_config 管理控制器
 * 
 * @author acooly
 * Date: 2017-03-01 00:53:18
 */
@Controller
@RequestMapping(value = "/manage/module/portlet/portletConfig")
public class PortletConfigManagerController extends AbstractJQueryEntityController<PortletConfig, PortletConfigService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private PortletConfigService portletConfigService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allTypes", PortletConfigTypeEnum.mapping());
	}

}
