/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年4月26日
 *
 */
package com.acooly.core.utils.constants;

import com.acooly.core.utils.ConfigurableConstants;

/**
 * @author zhangpu
 */
public class ConstantsB extends ConfigurableConstants {

	static {
		initWithProfile("b.properties");
	}

	public static final String KEY1 = getProperty("key1", "");
	public static final String KEY2 = getProperty("key2", "");
	public static final Integer AGE = getProperty("age", Integer.class, null);
}
