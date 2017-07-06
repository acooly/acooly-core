package com.acooly.module.captcha;

/** @author shuijing */
public interface Validatable<A, UA> {

  boolean validateTo(UA userAnswer);
}
