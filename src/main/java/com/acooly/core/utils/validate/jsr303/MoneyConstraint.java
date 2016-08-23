/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.jsr303;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Money数据类型的自定义JSR303标签
 * 
 * @author zhangpu
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { MoneyConstraintValidator.class })
public @interface MoneyConstraint {

	String message() default "{com.acooly.utils.validator.Money.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 最小值 <br/>
	 * 单位：分 <br/>
	 * 在校验时会包含此值
	 */
	long min() default 0;

	/**
	 * 最大值， <br/>
	 * 单位：分 <br/>
	 * 在校验时会包含此值
	 */
	long max() default Long.MAX_VALUE;

	/**
	 * 是否可以为null,默认不可为null
	 */
	boolean nullable() default false;

}
