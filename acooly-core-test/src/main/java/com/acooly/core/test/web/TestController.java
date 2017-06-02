/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-16 14:35 创建
 */
package com.acooly.core.test.web;

import com.acooly.core.test.domain.City;
import com.acooly.module.certification.CertificationService;
import com.acooly.module.mail.MailDto;
import com.acooly.module.mail.service.MailService;
import com.acooly.module.sms.SmsService;
import com.acooly.module.sms.sender.support.AliyunSmsSendVo;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** @author qiubo@yiji.com */
@RestController
@RequestMapping(value = "/test")
public class TestController {
  @Autowired private SmsService smsService;
  @Autowired private MailService mailService;

  @Value("${prop}")
  private String valueFromProp;

  @Autowired private CertificationService certificationService;

  @RequestMapping("sms")
  public void testSms() {
    smsService.send("15021507995", "xxx");
  }

  @RequestMapping("aliyunSms")
  public void testAliyunSms() {
    AliyunSmsSendVo asa = new AliyunSmsSendVo();

    Map<String, String> params = Maps.newHashMap();
    params.put("customer", "Testcustomer");
    asa.setFreeSignName("观世宇");
    asa.setSmsParamsMap(params);
    asa.setTemplateCode("SMS_67185863");

    smsService.send("18612299409", asa.toJson());
  }

  @GetMapping("testPermission")
  public Boolean testPermission() {
    return SecurityUtils.getSubject().isPermitted("xxxx");
  }

  @GetMapping("mail")
  public void testMail() {
    MailDto dto = new MailDto();
    dto.to("qiuboboy@qq.com")
        .subject("测试")
        .param("name", "x")
        .param("message", "how are you!")
        .templateName("register1");
    mailService.send(dto);
  }

  @GetMapping("testPojo")
  public City testPojo() {
    City city = new City();

    return city;
  }

  @RequestMapping("cert")
  public void testCert() {
    certificationService.certification("韦崇凯", "500221198810192313");
  }
}
