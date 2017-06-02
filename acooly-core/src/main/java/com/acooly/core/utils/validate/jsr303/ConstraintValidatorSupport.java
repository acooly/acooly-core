package com.acooly.core.utils.validate.jsr303;

public class ConstraintValidatorSupport {

  protected String message;

  protected boolean hasCustomMessage() {
    return this.message != null && "".equals(this.message);
  }
}
