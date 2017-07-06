package com.acooly.module.captcha;

import com.acooly.module.captcha.repository.CaptchaRepository;
import com.acooly.module.captcha.repository.RedisCaptchaRepository;
import com.acooly.module.captcha.support.StringCaseSensitiveValidator;
import com.acooly.module.captcha.support.StringValidator;
import com.acooly.module.captcha.support.generator.RandomWordCaptchaGenerator;
import com.acooly.module.captcha.support.generator.RandonNumberCaptchaGenerator;
import com.acooly.module.captcha.support.handler.ValidatableAnswerHandler;
import com.acooly.module.captcha.support.generator.DefaultUniqueGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import static com.acooly.module.captcha.CaptchaProperties.PREFIX;

/** @author shuijing */
@Configuration
@EnableConfigurationProperties({CaptchaProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.captcha")
public class CaptchaAutoConfig {

  @Autowired private CaptchaProperties properties;

  @Bean
  public CaptchaRepository redisCaptchaRepository(RedisTemplate redisTemplate) {
    return new RedisCaptchaRepository(redisTemplate);
  }

  @Bean
  public Validator stringValidator() {
    return new StringValidator();
  }

  @Bean
  public Validator stringCaseSensitiveValidator() {
    return new StringCaseSensitiveValidator();
  }

  @Bean
  public CaptchaGenerator randomWordCaptchaGenerator(CaptchaRepository redisCaptchaRepository) {
    RandomWordCaptchaGenerator generator = new RandomWordCaptchaGenerator();
    generator.setCaptchaRepository(redisCaptchaRepository);
    generator.setLength(properties.getCaptchaLength());
    generator.setDefaultSeconds(properties.getExpireSeconds());
    return generator;
  }

  @Bean
  public CaptchaGenerator randonNumberCaptchaGenerator(CaptchaRepository redisCaptchaRepository) {
    RandonNumberCaptchaGenerator generator = new RandonNumberCaptchaGenerator();
    generator.setCaptchaRepository(redisCaptchaRepository);
    generator.setLength(properties.getCaptchaLength());
    generator.setDefaultSeconds(properties.getExpireSeconds());
    return generator;
  }

  @Bean
  public DefaultUniqueGenerator defaultUniqueGenerator(CaptchaRepository redisCaptchaRepository) {
    DefaultUniqueGenerator generator = new DefaultUniqueGenerator(properties.getCaptchaLength());
    generator.setCaptchaRepository(redisCaptchaRepository);
    generator.setDefaultSeconds(properties.getExpireSeconds());
    return generator;
  }

  @Bean
  public AnswerHandler validatableAnswerHandler(
      CaptchaRepository redisCaptchaRepository, Validator stringValidator) {
    return new ValidatableAnswerHandler(redisCaptchaRepository, stringValidator);
  }

  @Bean
  public AnswerHandler caseSensitiveHandler(
      CaptchaRepository redisCaptchaRepository, Validator stringCaseSensitiveValidator) {
    return new ValidatableAnswerHandler(redisCaptchaRepository, stringCaseSensitiveValidator);
  }
}
