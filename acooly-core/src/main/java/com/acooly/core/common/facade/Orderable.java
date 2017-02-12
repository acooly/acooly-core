/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import java.io.Serializable;

/**
 * @author zhangpu
 */
public interface Orderable extends Serializable {
	String getGid();

	String getPartnerId();

	void check();

}
