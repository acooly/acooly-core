/**
 * create by zhangpu
 * date:2015年11月27日
 */
package com.acooly.module.lottery.enums;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author zhangpu
 * @date 2015年11月27日
 */
public enum MaxPeriod {

	ulimit("ulimit", "无限制", ""),

	day("day", "按天", "yyyyMMdd"),

	week("week", "按周", "yyyy"),

	month("month", "按月", "yyyyMM"),

	quarter("quarter", "按季", "yyyy"),

	year("year", "按年", "yyyy");

	private String code;
	private String message;
	private String patten;

	private MaxPeriod(String code, String message, String patten) {
		this.code = code;
		this.message = message;
		this.patten = patten;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getPatten() {
		return patten;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (MaxPeriod type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	@Override
	public String toString() {
		return this.code + " : " + this.message;
	}

}
