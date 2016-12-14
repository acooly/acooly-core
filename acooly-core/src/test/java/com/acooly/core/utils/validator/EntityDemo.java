/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validator;

import javax.validation.constraints.Size;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.jsr303.CertNo;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.core.utils.validate.jsr303.MobileNo;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;

/**
 * @author zhangpu
 */
public class EntityDemo {

	@CertNo(message="asdfasd")
	private String certNo;

	@MobileNo(blankable = true)
	private String mobileNo;

	@MoneyConstraint(min = 100, max = 10000, message = "金额不合法")
	private Money amount;

	@Size(min = 10, max = 255)
	@HttpUrl
	private String url;

	@Size(min = 16, max = 64, message = "订单号长度错误")
	private String orderNo;

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
