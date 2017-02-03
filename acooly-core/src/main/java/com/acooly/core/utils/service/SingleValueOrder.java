/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:08 创建
 */
package com.acooly.core.utils.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author qiubo@yiji.com
 */
@Setter
@Getter
public class SingleValueOrder<T> extends OrderBase {
	@Valid
	@NotNull
	private T dto;
}
