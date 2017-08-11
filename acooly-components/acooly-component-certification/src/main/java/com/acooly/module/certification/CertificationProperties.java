/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-24 21:59 创建
 */
package com.acooly.module.certification;

import com.acooly.core.utils.enums.Messageable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.acooly.module.certification.CertificationProperties.BankCertProvider.ALI;
import static com.acooly.module.certification.CertificationProperties.PREFIX;

/**
 * @author zhike@yiji.com
 * @author shuijing
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
@Validated
public class CertificationProperties {
  public static final String PREFIX = "acooly.certification";

  /** 实名权限code */
  @NotBlank private String appCode;

  /** 实名认证 */
  @NotNull private Provider provider = Provider.ALI;

  /** 银行卡认证，默认为阿里云 */
  private BankCertProvider bankCertProvider = ALI;

  /** 实名认证服务地址 */
  private String url = "http://idcard.market.alicloudapi.com";

  private int timeout = 20000;

  public enum Provider implements Messageable {
    /** 阿里实名认证 */
    ALI("aliRealNameAuthentication", "阿里实名认证");

    private final String code;
    private final String message;

    Provider(String code, String message) {
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

  public enum BankCertProvider implements Messageable {
    /** 阿里银行卡二三四要素认证 */
    ALI("aliBankCardCertService", "阿里银行卡二三四要素认证");
    private final String code;
    private final String message;

    BankCertProvider(String code, String message) {
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
