/**
 * create by zhangpu
 * date:2015年6月15日
 */
package com.acooly.module.appopenapi.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author zhangpu
 *
 */
public enum CommissionType {

	withdraw("withdraw", "提现手续费");

	private String code;
	private String message;

	private CommissionType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (CommissionType type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	@Override
	public String toString() {
		return this.code + " : " + this.message;
	}
}