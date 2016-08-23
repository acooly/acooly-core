/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年4月26日
 *
 */
package com.acooly.core.utils.constants;

import java.util.Date;

import com.acooly.core.utils.ConfigurableConstants;

/**
 * @author zhangpu
 */
public class ConstantsA extends ConfigurableConstants {

	static {
		initWithProfile("a.properties");
	}

	public static final String KEY1 = getProperty("key1", "");
	public static final String KEY2 = getProperty("key2", "");
	public static final int AGE = getProperty("age", Integer.class, 0);
	public static final Date BIRTHDAY = getProperty("birthday", Date.class, null);
}
