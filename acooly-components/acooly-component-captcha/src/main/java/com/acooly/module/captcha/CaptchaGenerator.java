package com.acooly.module.captcha;

import com.acooly.module.captcha.exception.CaptchaGenerateException;

/** @author shuijing */
public interface CaptchaGenerator<A> {
  Captcha<A> createCaptcha(String key, Long seconds) throws CaptchaGenerateException;
}
