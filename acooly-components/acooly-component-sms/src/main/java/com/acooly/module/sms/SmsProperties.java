/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-24 21:59 创建
 */
package com.acooly.module.sms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.acooly.module.sms.SmsProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
@Validated
public class SmsProperties {
	public static final String PREFIX = "acooly.sms";
	
	public boolean enable;
	/**
	 * 发送服务beanId
	 */
	@NotNull
	private String provider;
	private String url;
	private String username;
	
	private String password;
	private String batchUser;
	private String batchPswd;
	private String prefix;
	private String posfix;
	private int timeout = 20000;
	/**
	 * IP最大频率(分钟)
	 */
	private int ipFreq = 200;
	
	/**
	 * 单号码发送间隔（秒）
	 */
	private int sendInterval = 10;
	private List<String> blacklist = Lists.newArrayList();
	/**
	 * 短信模板定义
	 */
	private Map<String, String> template = Maps.newHashMap();
	
	private int threadMin = 1;
	private int threadMax = 20;
	private int threadQueue = 20;
	
}
