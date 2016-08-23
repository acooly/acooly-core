/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.acooly.core.utils.validate.Validators;

/**
 * 内部通讯请求参数 基类
 * 
 * @author zhangpu
 */
public class OrderBase implements Orderable {

	/** serialVersionUID */
	private static final long serialVersionUID = 6062457604679752587L;

	@NotNull
	@Length(min = 1, max = 20)
	private String partnerId;

	@NotNull
	@Length(min = 20, max = 64)
	private String gid;

	public void check() {
		Validators.assertJSR303(this);
	}

	public void checkWithGroup(Class<?>... groups) {
		Validators.assertJSR303(this, null, groups);
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}
