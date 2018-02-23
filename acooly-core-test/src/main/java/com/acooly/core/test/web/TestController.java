/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-16 14:35 创建
 */
package com.acooly.core.test.web;

import com.acooly.core.test.dao.AppDao;
import com.acooly.core.test.domain.App;
import com.acooly.core.test.domain.City;
import com.acooly.module.certification.CertificationService;
import com.acooly.module.mail.MailDto;
import com.acooly.module.mail.service.MailService;
import com.acooly.module.sms.SmsService;
import com.acooly.module.sms.sender.support.AliyunSmsSendVo;
import com.acooly.module.sms.sender.support.CloopenSmsSendVo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/** @author qiubo@yiji.com */
@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {
  @Autowired private SmsService smsService;
  @Autowired private MailService mailService;
  @Autowired private DataSource dataSource;
  @Autowired private AppDao appDao;

  @Value("${prop}")
  private String valueFromProp;

  @Autowired private CertificationService certificationService;

  @RequestMapping("app")
  @Transactional
  public App testApp(App app) {
    appDao.save(app);
    return appDao.get(app.getId());
  }

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

  @RequestMapping("cloopenSms")
  public void testCloopen() {
    CloopenSmsSendVo col = new CloopenSmsSendVo();

    col.setTemplateId("181976");
    List<String> data = new ArrayList<>();
    data.add("17060915020001800000");
    data.add("145317");
    col.setDatas(data);

    smsService.send("18612299409", col.toJson());
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

  @GetMapping("testDataSource")
  public void testDataSource() throws Exception {
    Connection connection = dataSource.getConnection();
    String sql = "select now()";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      log.info("now={}", resultSet.getString(1));
    }
    connection.close();

    preparedStatement = connection.prepareStatement(sql);
    resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      log.info("now={}", resultSet.getString(1));
    }
  }

  @RequestMapping("cert")
  public void testCert() {
    certificationService.certification("韦崇凯", "500221198810192313");
  }

  @RequestMapping("404")
  public void test404() {}

  @RequestMapping("500")
  public void test500() {
      throw new RuntimeException("test 500");
  }
}
