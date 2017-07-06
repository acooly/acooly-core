package com.acooly.module.captcha.support.handler;

import com.acooly.module.captcha.AnswerHandler;
import com.acooly.module.captcha.Captcha;
import com.acooly.module.captcha.CaptchaGenerator;
import com.acooly.module.captcha.CaptchaProperties;
import com.acooly.module.captcha.dto.AnswerDto;
import com.acooly.module.captcha.exception.CaptchaGenerateException;
import com.acooly.module.captcha.exception.CaptchaValidateException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/** @author shuijing */
public class AnswerProxy<UA>
    implements AnswerHandler<UA>,
        ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

  private ApplicationContext applicationContext;

  private AnswerHandler<UA> handler;

  @Autowired private CaptchaProperties properties;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (handler == null) {
      handler =
          (AnswerHandler<UA>) this.applicationContext.getBean(properties.getGeneratorType().code());
    }
  }

  @Override
  public boolean isValid(AnswerDto<UA> answerDto) throws CaptchaValidateException {
    return handler.isValid(answerDto);
  }
}
