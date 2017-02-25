/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-16 14:35 创建
 */
package com.acooly.core.test.web;

import com.acooly.module.mail.MailDto;
import com.acooly.module.mail.MailService;
import com.acooly.module.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiubo@yiji.com
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
	@Autowired
	private SmsService smsService;
	@Autowired
	private MailService mailService;
	
	@RequestMapping("sms")
	public void testSms() {
		smsService.send("18580039996", "xxx");
	}
	
	@GetMapping("mail")
	public void testMail() {
		MailDto dto = new MailDto();
		dto.to("qiuboboy@qq.com").subject("测试").param("name", "x").param("message", "how are you!")
			.templateName("register1");
		mailService.send(dto);
	}
}
