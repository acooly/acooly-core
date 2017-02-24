/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-08-14
 *
 */
package com.acooly.module.portlet.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * site_action_log Channel 枚举定义
 * 
 * @author acooly Date: 2016-08-14 22:10:17
 */
public enum ActionChannel implements Messageable {

	wechat("wechat", "微信"),

	web("web", "网站"),
	
	mweb("mweb","移动web"),

	android("android", "安卓"),

	ios("ios", "苹果"),

	other("other", "其他");

	private final String code;
	private final String message;

	ActionChannel(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (ActionChannel type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	/**
	 * 通过枚举值码查找枚举值。
	 * 
	 * @param code
	 *            查找枚举值的枚举值码。
	 * @return 枚举值码对应的枚举值。
	 * @throws IllegalArgumentException
	 *             如果 code 没有对应的 Status 。
	 */
	public static ActionChannel find(String code) {
		for (ActionChannel status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Channel not legal:" + code);
	}

	/**
	 * 获取全部枚举值。
	 * 
	 * @return 全部枚举值。
	 */
	public static List<ActionChannel> getAll() {
		List<ActionChannel> list = new ArrayList<ActionChannel>();
		for (ActionChannel status : values()) {
			list.add(status);
		}
		return list;
	}

	/**
	 * 获取全部枚举值码。
	 * 
	 * @return 全部枚举值码。
	 */
	public static List<String> getAllCode() {
		List<String> list = new ArrayList<String>();
		for (ActionChannel status : values()) {
			list.add(status.code());
		}
		return list;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", this.code, this.message);
	}

}
