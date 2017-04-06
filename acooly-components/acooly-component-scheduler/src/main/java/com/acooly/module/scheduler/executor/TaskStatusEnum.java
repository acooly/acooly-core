package com.acooly.module.scheduler.executor;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * @author shuijing
 */
public enum TaskStatusEnum {

	NORMAL("NORMAL", "正常执行"),
	CANCELED("CANCELED", "已经作废");

	@Setter
	@Getter
	private String code;

	@Setter
	@Getter
	private String message;

	TaskStatusEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static TaskStatusEnum getEnumByCode(String code) {
		for (TaskStatusEnum t : TaskStatusEnum.values()) {
			if (StringUtils.equalsIgnoreCase(t.code, code)) {
				return t;
			}
		}
		return null;
	}
	
}
