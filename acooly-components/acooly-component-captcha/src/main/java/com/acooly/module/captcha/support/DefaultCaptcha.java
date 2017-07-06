package com.acooly.module.captcha.support;

import com.acooly.module.captcha.Answer;
import com.acooly.module.captcha.Captcha;
import lombok.Data;

/** @author shuijing */
@Data
public class DefaultCaptcha<A> implements Captcha<A> {

  private Answer<A> answer;

  private String id;

  private long expiredTimeMillis;

  public DefaultCaptcha(String id, Answer<A> answer, long expiredTimeMillis) {
    this.id = id;
    this.answer = answer;
    this.expiredTimeMillis = expiredTimeMillis;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public Answer<A> getAnswer() {
    return this.answer;
  }

  @Override
  public long getExpiredTimeMillis() {
    return this.expiredTimeMillis;
  }
}
