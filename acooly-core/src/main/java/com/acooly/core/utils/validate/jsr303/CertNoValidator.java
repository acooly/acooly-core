/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.jsr303;

import com.acooly.core.utils.validate.predicate.CertNoPredicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 身份证号码验证器
 *
 * @author zhangpu
 */
public class CertNoValidator extends ConstraintValidatorSupport
    implements ConstraintValidator<CertNo, String> {
  private boolean blankable;

  @Override
  public void initialize(CertNo constraintAnnotation) {
    this.blankable = constraintAnnotation.blankable();
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0) {
      if (!this.blankable && hasCustomMessage()) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                "{org.hibernate.validator.constraints.NotEmpty.message}")
            .addConstraintViolation();
        return false;
      } else {
        return true;
      }
    }

    if (!CertNoPredicate.INSTANCE.apply(value)) {
      if (hasCustomMessage()) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                "{com.acooly.utils.validator.CertNo.invalid.message}")
            .addConstraintViolation();
      }
      return false;
    }
    return true;
  }
}
