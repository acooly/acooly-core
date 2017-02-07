/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-07 17:55 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.enums.ResultStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubo@yiji.com
 */
@Getter
@Setter
public class PageResult<T> extends ResultBase {
	private PageInfo<T> dto;
	
	public static <T> PageResult<T> from(PageInfo<T> pageInfo) {
		PageResult<T> result = new PageResult<>();
		result.setDto(pageInfo);
		result.setStatus(ResultStatus.success);
		return result;
	}
}
