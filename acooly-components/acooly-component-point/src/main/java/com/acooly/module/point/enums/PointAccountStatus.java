/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 *
 */
package com.acooly.module.point.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.acooly.core.utils.enums.Messageable;

/**
 * 积分账户 Status 枚举定义
 * 
 * @author cuifuqiang
 * Date: 2017-02-03 00:28:30
 */
public enum PointAccountStatus implements Messageable {

		invalid("invalid", "无效"),
		valid("valid", "有效"),
	;

	private final String code;
	private final String message;

	private PointAccountStatus(String code, String message) {
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
		for (PointAccountStatus type : values()) {
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
	public static PointAccountStatus find(String code) {
		for (PointAccountStatus status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Status not legal:" + code);
	}

	/**
	 * 获取全部枚举值。
	 * 
	 * @return 全部枚举值。
	 */
	public static List<PointAccountStatus> getAll() {
		List<PointAccountStatus> list = new ArrayList<PointAccountStatus>();
		for (PointAccountStatus status : values()) {
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
		for (PointAccountStatus status : values()) {
			list.add(status.code());
		}
		return list;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", this.code, this.message);
	}

}
