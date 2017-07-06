package com.acooly.module.captcha;

import java.io.Serializable;

/** @author shuijing */
public interface Captcha<A> extends Serializable {

  String getId();

  Answer<A> getAnswer();

  long getExpiredTimeMillis();
}
