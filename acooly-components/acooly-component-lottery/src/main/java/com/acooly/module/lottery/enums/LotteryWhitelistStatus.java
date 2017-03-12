package com.acooly.module.lottery.enums;

import java.util.Map;

import com.google.common.collect.Maps;

public enum LotteryWhitelistStatus {

	enable("enable", "正常"),

	disable("disable", "禁用"),

	finish("finish", "完成");

	private String code;
	private String message;

	private LotteryWhitelistStatus(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static LotteryWhitelistStatus codeOf(String code) {
		for (LotteryWhitelistStatus status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		return null;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (LotteryWhitelistStatus type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

}
