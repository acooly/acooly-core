package com.acooly.module.lottery.enums;

import java.util.Map;

import com.google.common.collect.Maps;

public enum LotteryType {

	roulette("roulette", "轮盘抽奖"),

	other("other", "其他");

	private String code;
	private String message;

	private LotteryType(String code, String message) {
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

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (LotteryType type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

}
