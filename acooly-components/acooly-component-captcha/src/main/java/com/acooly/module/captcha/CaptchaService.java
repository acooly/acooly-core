package com.acooly.module.captcha;

import com.acooly.module.captcha.exception.CaptchaGenerateException;
import com.acooly.module.captcha.exception.CaptchaValidateException;

/**
 * 验证码
 *
 * @author shuijing
 */
public interface CaptchaService {

  Captcha getCaptcha() throws CaptchaGenerateException;

  Captcha getCaptcha(Long seconds) throws CaptchaGenerateException;

  Captcha getCaptcha(String key, Long seconds) throws CaptchaGenerateException;

  Captcha getCaptcha(String key, String businessCode, Long seconds) throws CaptchaGenerateException;

  void validateCaptcha(String captchaId, String userAnswer) throws CaptchaValidateException;
}
