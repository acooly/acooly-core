package com.acooly.module.captcha.support.generator;

import com.acooly.module.captcha.Captcha;
import com.acooly.module.captcha.CaptchaGenerator;
import com.acooly.module.captcha.repository.CaptchaRepository;
import com.acooly.module.captcha.Validator;
import com.acooly.module.captcha.exception.CaptchaValidateException;
import com.acooly.module.captcha.support.DefaultCaptcha;
import com.acooly.module.captcha.support.ValidatableAnswer;
import lombok.Setter;

/** @author shuijing */
@Setter
public abstract class AbstractCaptchaGenerator<A, UA> implements CaptchaGenerator<A> {

  private Validator<A, UA> validator;

  private CaptchaRepository<String, Captcha> captchaRepository;

  private Long defaultSeconds;

  protected Captcha<A> createCaptcha(String id, A answer, Long seconds)
      throws CaptchaValidateException {

    seconds = seconds == null ? defaultSeconds : seconds;
    long expiredTimeMillis = System.currentTimeMillis() + seconds * 1000;
    ValidatableAnswer<A, UA> validatableAnswer = new ValidatableAnswer<>(answer, validator);
    DefaultCaptcha<A> captcha = new DefaultCaptcha<>(id, validatableAnswer, expiredTimeMillis);

    captchaRepository.set(id, captcha, seconds);

    return captcha;
  }
}
