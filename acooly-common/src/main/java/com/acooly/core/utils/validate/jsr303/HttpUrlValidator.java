/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.jsr303;

import com.acooly.core.utils.validate.predicate.HttpUrlPredicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zhangpu
 */
public class HttpUrlValidator extends ConstraintValidatorSupport
        implements ConstraintValidator<HttpUrl, String> {
    private boolean blankable;

    @Override
    public void initialize(HttpUrl constraintAnnotation) {
        blankable = constraintAnnotation.blankable();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            if (!blankable && hasCustomMessage()) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(
                                "{org.hibernate.validator.constraints.NotEmpty.message}")
                        .addConstraintViolation();
            }
            return blankable;
        }
        if (!HttpUrlPredicate.INSTANCE.apply(value)) {
            if (hasCustomMessage()) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(
                                "{com.acooly.utils.validator.HttpUrl.format.message}")
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
