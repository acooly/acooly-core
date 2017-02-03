/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-12-03 01:56 创建
 *
 */
package com.acooly.module.appservice.ex;


import com.acooly.core.common.exception.GeneralException;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.service.ResultBase;

/**
 * @author qzhanbo@yiji.com
 */
public class SystemExceptionExceptionHandler implements ExceptionHandler<GeneralException> {
	
	@Override
	public void handle(ExceptionContext<?> context, GeneralException e) {
		ResultBase res = context.getResponse();
		res.setDetail(e.getMessage());
		res.setStatus(ResultStatus.failure);
	}
}
