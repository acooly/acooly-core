/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.module.mail;

import com.google.common.base.Charsets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import static com.acooly.module.mail.MailProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
@Validated
public class MailProperties implements InitializingBean {
	public static final String PREFIX = "acooly.mail";
	private boolean enable = true;
	private boolean debug = false;
	@NotEmpty
	private String hostname;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	@NotEmpty
	private String fromName;
	@NotEmpty
	private String fromAddress;
	private final String charset = Charsets.UTF_8.name();
	private int threadMin = 1;
	private int threadMax = 20;
	private int threadQueue = 20;
	
	/**
	 * 模板路径，邮件模板放在此路径下<b> 文件名作为key(不带后缀)，文件内容为模板内容
	 */
	private final String mailTemplatePath = "classpath:/mail/";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
