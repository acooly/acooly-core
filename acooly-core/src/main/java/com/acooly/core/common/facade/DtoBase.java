/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:03 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.ToString;

import java.io.Serializable;

/**
 * @author qiubo@yiji.com
 */
public class DtoBase implements Serializable {
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
