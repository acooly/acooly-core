/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:03 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author qiubo@yiji.com
 */
public class DtoBase implements Serializable {
	
	/** 商户订单号 */
	private String merchOrderNo;
	/** 业务订单号 **/
	@NotEmpty
	private String bizOrderNo;
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
	
	/**
	 * 请求参数校验
	 */
	public void check() throws OrderCheckException {
		
	}

	public String getMerchOrderNo() {
		return merchOrderNo;
	}

	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}

	public String getBizOrderNo() {
		return bizOrderNo;
	}

	public void setBizOrderNo(String bizOrderNo) {
		this.bizOrderNo = bizOrderNo;
	}
}
