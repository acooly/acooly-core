package com.acooly.module.captcha.support.handler;

import com.acooly.module.captcha.*;
import com.acooly.module.captcha.dto.AnswerDto;
import com.acooly.module.captcha.exception.CaptchaValidateException;
import com.acooly.module.captcha.repository.CaptchaRepository;
import com.acooly.module.captcha.repository.RedisCaptchaRepository;
import com.acooly.module.captcha.support.ValidatableAnswer;
import lombok.extern.slf4j.Slf4j;

/** @author shuijing */
@Slf4j
public class ValidatableAnswerHandler<A, UA> implements AnswerHandler<UA> {

  private CaptchaRepository repository;

  public ValidatableAnswerHandler(CaptchaRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean isValid(AnswerDto<UA> answerDto) throws CaptchaValidateException {

    Captcha captcha = repository.get(answerDto.getCaptchaId());
    if (captcha == null) {
      throw new CaptchaValidateException("CAPCHA_IS_NULL", "验证码为空");
    }
    if (System.currentTimeMillis() > captcha.getExpiredTimeMillis()) {
      throw new CaptchaValidateException("CAPCHA_TIMEOUT", "验证码过期");
    }

    Answer answer = captcha.getAnswer();
    ValidatableAnswer<A, UA> validatableAnswer = (ValidatableAnswer) answer;
    boolean validated = validatableAnswer.validateTo(answerDto.getUserAnswer());

    if (!validated) {
      throw new CaptchaValidateException("CAPCHA_VERIFY_FAIL", "验证码不正确");
    }
    //      if (validated) {
    //        repository.delete(answerDto.getCaptchaId());
    //      }
    return validated;
  }
}
