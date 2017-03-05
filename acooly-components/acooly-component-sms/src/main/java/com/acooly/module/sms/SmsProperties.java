/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-24 21:59 创建
 */
package com.acooly.module.sms;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
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
	 * 短信服务提供商
	 */
	@NotNull
	private Provider provider;
	private String url;
	private String username;
	
	private String password;
	private String batchUser;
	private String batchPswd;
	private String prefix;
	private String posfix;
    /**
     * 仅当使用亿美通道时配置
     */
	private Emay emay;
	private int timeout = 20000;
	/**
	 * IP最大频率(分钟)
	 */
	private int ipFreq = 200;
	
	/**
	 * 单号码发送间隔（秒）
	 */
	private int sendInterval = 10;

    /**
     * 黑名单
     */
	private List<String> blacklist = Lists.newArrayList();
	/**
	 * 短信模板定义
	 */
	private Map<String, String> template = Maps.newHashMap();
	
	private int threadMin = 1;
	private int threadMax = 20;
	private int threadQueue = 20;
	
	@Data
	public static class Emay {
		/**
		 * 序列号,请通过亿美销售人员获取
		 */
		private String sn;
		/**
		 * 密码,请通过亿美销售人员获取
		 */
		private String password;
	}
	
	public enum Provider implements Messageable {
													/**
													 * 亿美
													 */
													EMAY("emayShortMessageSender", "亿美"),
													/**
													 * 漫道
													 */
													MAIDAO("maidaoShortMessageSender", "漫道"),
													/**
													 * 重庆客亲通
													 */
													KLUM("chinaklumShortMessageSender", "重庆客亲通"),
													/**
													 * 测试
													 */
													MOCK("mockShortMessageSender", "测试");
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
	
	@PostConstruct
	public void init() {
		if (this.provider == Provider.EMAY) {
			Assert.notNull(this.emay);
			Assert.hasText(this.emay.getSn());
			Assert.hasText(this.emay.getPassword());
		}
	}
}
