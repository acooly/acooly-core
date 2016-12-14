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
 * 身份证号码验证标签
 * 
 * @author zhangpu
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { CertNoValidator.class })
public @interface CertNo {
	String message() default "{com.acooly.utils.validator.CertNo.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean blankable() default false;

}
