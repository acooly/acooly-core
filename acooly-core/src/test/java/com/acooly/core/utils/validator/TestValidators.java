/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validator;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.Validators;

/**
 * @author zhangpu
 */
public class TestValidators {

	public static void main(String[] args) {
		// EntityDemo ed = new EntityDemo();
		// ed.setAmount(Money.amout("0.10"));
		// ed.setCertNo("110000197609260652aaa");
		// ed.setMobileNo("13796177630");
		// ed.setUrl("http://acooly.cn?name=zhangpu");
		// ed.setOrderNo("12345");
		// Validators.assertJSR303(ed);

		System.out.println(Money.amout("12,111.00"));
	}

}
