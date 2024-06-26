/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.jsr303;

import com.acooly.core.utils.Money;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Money 类型JSR303验证器
 *
 * @author zhangpu
 */
public class MoneyConstraintValidator extends ConstraintValidatorSupport
        implements ConstraintValidator<MoneyConstraint, Money> {

    private long min;
    private long max;
    private boolean nullable;

    @Override
    public void initialize(MoneyConstraint constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        nullable = constraintAnnotation.nullable();
        this.message = constraintAnnotation.message();
        if (min < 0) {
            throw new RuntimeException("金额约束最小值不能小于零");
        }
        if (max <= min) {
            throw new RuntimeException("金额约束最大值必须大于最小值");
        }
    }

    @Override
    public boolean isValid(com.acooly.core.utils.Money value, ConstraintValidatorContext context) {

        if (value == null) {
            if (!nullable) {
                if (hasCustomMessage()) {
                    context.disableDefaultConstraintViolation();
                    context.unwrap(HibernateConstraintValidatorContext.class)
                            .addMessageParameter("min", Money.cent(min))
                            .addMessageParameter("max", Money.cent(max))
                            .buildConstraintViolationWithTemplate(
                                    "{com.acooly.utils.validator.Money.message}")
                            .addConstraintViolation();

                }
            }
            return nullable;
        }

        if (value.getCent() < min) {
            if (hasCustomMessage()) {
                context.disableDefaultConstraintViolation();
                context.unwrap(HibernateConstraintValidatorContext.class)
                        .addMessageParameter("value", Money.cent(min))
                        .buildConstraintViolationWithTemplate(
                                "{javax.validation.constraints.Min.message}")
                        .addConstraintViolation();
            }

            return false;
        }
        if (value.getCent() >= max) {
            if (hasCustomMessage()) {
                context.disableDefaultConstraintViolation();
                context.unwrap(HibernateConstraintValidatorContext.class)
                        .addMessageParameter("value", Money.cent(max))
                        .buildConstraintViolationWithTemplate(
                                "{javax.validation.constraints.Max.message}")
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
