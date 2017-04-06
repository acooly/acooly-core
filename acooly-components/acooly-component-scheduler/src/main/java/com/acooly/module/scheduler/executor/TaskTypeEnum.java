package com.acooly.module.scheduler.executor;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shuijing
 */
public enum TaskTypeEnum {
	
	LOCAL_TASK("LOCAL", "本地任务"),
	HTTP_TASK("HTTP", "HTTP调用的任务"),
	PLAYBOOK_TASK("PLAYBOOK", "PLAYBOOK任务");
	
	@Setter
	@Getter
	private String code;
	
	@Setter
	@Getter
	private String message;
	
	TaskTypeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static TaskTypeEnum getEnumByCode(String code) {
		for (TaskTypeEnum t : TaskTypeEnum.values()) {
			if (StringUtils.equalsIgnoreCase(t.code, code)) {
				return t;
			}
		}
		return null;
	}
	
}
