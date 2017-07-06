package com.acooly.module.captcha.support;

import com.acooly.module.captcha.Validatable;
import com.acooly.module.captcha.Validator;
import lombok.Getter;
import lombok.Setter;

/** @author shuijing */
@Getter
@Setter
public class ValidatableAnswer<A, UA> extends DefaultAnswer<A> implements Validatable<A, UA> {

  private Validator<A, UA> validator;

  public ValidatableAnswer(A value, Validator<A, UA> validator) {
    super(value);
    this.validator = validator;
  }

  @Override
  public boolean validateTo(UA userAnswer) {
    return validator.validate(getValue(), userAnswer);
  }
}
