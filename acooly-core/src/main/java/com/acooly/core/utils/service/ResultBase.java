/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.service;

import com.acooly.core.utils.ToString;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.ResultStatus;

/**
 * @author zhangpu
 */
public class ResultBase extends LinkedHashMapParameterize<String, Object> implements Resultable {

	/** serialVersionUID */
	private static final long serialVersionUID = -8702480923545642017L;

	private Messageable status = ResultStatus.success;

	private String detail;

	public Messageable getStatus() {
		return status;
	}

	public void setStatus(Messageable status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
