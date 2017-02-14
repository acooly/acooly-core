/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-12-03 00:41 创建
 *
 */
package com.acooly.module.appservice.ex;


import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.common.facade.ResultBase;

/**
 * @author qzhanbo@yiji.com
 */
public class OrderCheckExceptionHandler implements ExceptionHandler<OrderCheckException> {
	
	@Override
	public void handle(ExceptionContext<?> context, OrderCheckException ex) {
		ResultBase res = context.getResponse();
		res.setDetail(ex.getMessage());
		res.setStatus(ResultStatus.failure);
	}
}