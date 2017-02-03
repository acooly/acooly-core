/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-10-10 20:21 创建
 */
package com.acooly.core.test.appservice;

import com.acooly.core.utils.service.ResultBase;
import com.acooly.core.utils.service.SingleValueOrder;
import com.acooly.core.utils.service.SingleValueResult;
import com.acooly.module.appservice.AppService;

/**
 * @author qiubo@yiji.com
 */
@AppService
public class AppServiceTestService {
	
	public SingleValueResult<AppDto> test(SingleValueOrder<AppDto> orderBase) {
		//do what you like
		return SingleValueResult.from(orderBase.getDto());
	}
	
	@AppService.ValidationGroup(AppDto.Test1.class)
	public SingleValueResult<AppDto> test1(SingleValueOrder<AppDto> orderBase) {
		//do what you like
		return SingleValueResult.from(orderBase.getDto());
	}
	
	@AppService.ValidationGroup(AppDto.Test2.class)
	public SingleValueResult<AppDto> test2(SingleValueOrder<AppDto> orderBase) {
		//do what you like
		return SingleValueResult.from(orderBase.getDto());
	}
	
	@AppService.ValidationGroup(value = AppDto.Test2.class, checkDefaultGroup = false)
	public SingleValueResult<AppDto> test3(SingleValueOrder<AppDto> orderBase) {
		//do what you like
		return SingleValueResult.from(orderBase.getDto());
	}
}
