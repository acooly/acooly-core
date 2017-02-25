/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 11:26 创建
 */
package com.acooly.module.mail;

import com.acooly.core.utils.validate.Validators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qiubo@yiji.com
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
	@Autowired
	private MailTemplateService mailTemplateService;
	@Autowired
	private MailProperties mailProperties;
	@Resource(name = "mailTaskExecutor")
	private TaskExecutor taskExecutor;
	
	@Override
	public void send(MailDto dto) {
		log.info("发送邮件:{}", dto);
		String content = validateAndParse(dto);
		send0(dto, content);
	}
	
	@Override
	public void sendAsync(MailDto dto) {
		log.info("发送邮件:{}", dto);
		String content = validateAndParse(dto);
		taskExecutor.execute(() -> send0(dto, content));
	}
	
	public String validateAndParse(MailDto dto) {
		Validators.assertJSR303(dto);
		dto.getTo().forEach(EmailValidator.getInstance()::isValid);
		return mailTemplateService.parse(dto.getTemplateName(), dto.getParams());
	}
	
	private void send0(MailDto dto, String content) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setDebug(mailProperties.isDebug());
			email.setHostName(mailProperties.getHostname());
			email.setAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
			
			email.setFrom(mailProperties.getFromAddress(), mailProperties.getFromName());
			email.setSSLOnConnect(true);
			for (String toMail : dto.getTo()) {
				email.addTo(toMail);
			}
			email.setSubject(dto.getSubject());
			email.setHtmlMsg(content);
			email.setCharset(mailProperties.getCharset());
			email.send();
		} catch (Exception e) {
			log.error("发送邮件失败", e);
		}
	}
	
}
