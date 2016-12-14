/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.jsr303;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.acooly.core.utils.validate.predicate.MobileNoPredicate;

/**
 * @author zhangpu
 */
public class MobileNoValidator extends ConstraintValidatorSupport implements ConstraintValidator<MobileNo, String> {

	private boolean blankable;

	@Override
	public void initialize(MobileNo constraintAnnotation) {
		blankable = constraintAnnotation.blankable();
		this.message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.length() == 0) {
			if (!blankable) {
				if (hasCustomMessage()) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(
					        "{org.hibernate.validator.constraints.NotBlank.message}").addConstraintViolation();
				}
			}
			return blankable;
		}
		if (!MobileNoPredicate.INSTANCE.apply(value)) {
			if (hasCustomMessage()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{com.acooly.utils.validator.MobileNo.format.message}")
				        .addConstraintViolation();
			}
			return false;
		}
		return true;
	}
}
