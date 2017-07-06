package com.acooly.module.captcha;

import com.acooly.core.utils.enums.Messageable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import static com.acooly.module.captcha.CaptchaProperties.PREFIX;

/** @author shuijing */
@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = PREFIX)
public class CaptchaProperties {
  public static final String PREFIX = "acooly.captcha";

  public static final int DEFAULT_LENGTH = 4;

  public boolean enable;

  /** 过期时间,单位秒,默认300S(5分钟) */
  private Long expireSeconds = 5 * 60L;

  private int captchaLength = DEFAULT_LENGTH;

  private GeneratorType generatorType = GeneratorType.RandonNumber;

  public enum GeneratorType implements Messageable {
    RandomWord("randomWordCaptchaGenerator", "随机字符"),

    RandonNumber("randonNumberCaptchaGenerator", "随机数字");

    private final String code;
    private final String message;

    GeneratorType(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String code() {
      return this.code;
    }

    @Override
    public String message() {
      return this.message;
    }
  }

  public enum AnswerHandlerType implements Messageable {
    DefualtAnswerHandler("validatableAnswerHandler", "默认字符验证器");

    private final String code;
    private final String message;

    AnswerHandlerType(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String code() {
      return this.code;
    }

    @Override
    public String message() {
      return this.message;
    }
  }
}
