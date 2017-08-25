/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by zhangpu
* date:2017-08-21
*/
package com.acooly.module.store.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.store.entity.Category;
import com.acooly.module.store.service.CategoryService;

import com.google.common.collect.Maps;

/**
 * 品类信息 管理控制器
 * 
 * @author zhangpu
 * Date: 2017-08-21 01:56:34
 */
@Controller
@RequestMapping(value = "/manage/store/category")
public class CategoryManagerController extends AbstractJQueryEntityController<Category, CategoryService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private CategoryService categoryService;

	

}