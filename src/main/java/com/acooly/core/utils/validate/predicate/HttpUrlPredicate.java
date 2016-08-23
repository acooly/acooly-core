/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.predicate;

import org.apache.commons.validator.routines.UrlValidator;

import com.acooly.core.utils.Strings;
import com.google.common.base.Predicate;

/**
 * Http Url格式断言
 * 
 * @author zhangpu
 */
public final class HttpUrlPredicate implements Predicate<String> {

	public static final HttpUrlPredicate INSTANCE = new HttpUrlPredicate();

	private HttpUrlPredicate() {
	}

	private UrlValidator httpUrlValidator = new UrlValidator(new String[] { "http", "https" });

	@Override
	public boolean apply(String input) {
		return !Strings.isBlank(input) && httpUrlValidator.isValid(input);
	}

}
