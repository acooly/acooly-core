/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by qiubo
* date:2018-06-27
*/
package com.acooly.module.eav.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.eav.entity.EavSchema;
import com.acooly.module.eav.service.EavSchemaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * eav_schema 管理控制器
 * 
 * @author qiubo
 * Date: 2018-06-27 14:04:57
 */
@Controller
@RequestMapping(value = "/manage/module/eav/eavSchema")
public class EavSchemaManagerController extends AbstractJQueryEntityController<EavSchema, EavSchemaEntityService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private EavSchemaEntityService eavSchemaEntityService;

	

}