package com.acooly.module.captcha.support.handler;

import com.acooly.module.captcha.*;
import com.acooly.module.captcha.dto.AnswerDto;
import com.acooly.module.captcha.exception.CaptchaValidateException;
import com.acooly.module.captcha.repository.CaptchaRepository;
import lombok.extern.slf4j.Slf4j;

/** @author shuijing */
@Slf4j
public class ValidatableAnswerHandler<V, UA> implements AnswerHandler<UA> {

  private CaptchaRepository repository;

  private Validator<V, UA> validator;

  public ValidatableAnswerHandler(CaptchaRepository repository, Validator validator) {
    this.repository = repository;
    this.validator = validator;
  }

  @Override
  public boolean isValid(AnswerDto<UA> answerDto) throws CaptchaValidateException {

    Captcha<V> captcha = repository.get(answerDto.getCaptchaId());
    if (captcha == null) {
      throw new CaptchaValidateException("CAPCHA_IS_NULL", "验证码为空");
    }
    if (System.currentTimeMillis() > captcha.getExpiredTimeMillis()) {
      throw new CaptchaValidateException("CAPCHA_TIMEOUT", "验证码过期");
    }

    boolean validated = validator.validate(captcha.getValue(), answerDto.getUserAnswer());

    if (!validated) {
      throw new CaptchaValidateException("CAPCHA_VERIFY_FAIL", "验证码不正确");
    }
    //      if (validated) {
    //        repository.delete(answerDto.getCaptchaId());
    //      }
    return validated;
  }
}
