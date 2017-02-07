/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:16 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.ResultStatus;

/**
 * @author qiubo@yiji.com
 */
public class SingleResult<T> extends ResultBase {
	private T dto;
	
	public T getDto() {
		return dto;
	}
	
	public void setDto(T dto) {
		this.dto = dto;
	}
	
	public static <T> SingleResult<T> from(T dto) {
		SingleResult<T> singleResult = new SingleResult<>();
		singleResult.setDto(dto);
		singleResult.setStatus(ResultStatus.success);
		return singleResult;
	}
}
