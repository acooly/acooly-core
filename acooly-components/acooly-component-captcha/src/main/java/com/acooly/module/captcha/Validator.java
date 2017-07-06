package com.acooly.module.captcha;

/** @author shuijing */
public interface Validator<A, UA> {
  boolean validate(A answer, UA userAnswer);
}
