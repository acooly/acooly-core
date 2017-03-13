/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by cuifuqiang
* date:2017-02-03
*/
package com.acooly.module.point.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.point.enums.PointTradeType;

/**
 * 积分交易信息 Entity
 *
 * @author cuifuqiang Date: 2017-02-03 22:50:14
 */
@Entity
@Table(name = "point_trade")
public class PointTrade extends AbstractEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** id */
	/** 交易订单号 */
	private String tradeNo;
	/** 交易类型 */
	@Enumerated(EnumType.STRING)
	private PointTradeType tradeType;
	/** 用户名 */
	private String userName;
	/** 积分账户ID */
	private Long accountId;
	/** 交易积分 */
	private Long amount = 0l;
	/** 交易后冻结积分 */
	private Long endFreeze = 0l;
	/** 交易后积分 */
	private Long endBalance = 0l;
	/** 交易后有效积分 */
	private Long endAvailable = 0l;
	/** 相关业务数据 */
	private String businessData;
	/** 备注 */
	private String memo;

	public String getTradeNo() {
		return this.tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public PointTradeType getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(PointTradeType tradeType) {
		this.tradeType = tradeType;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAmount() {
		return this.amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getEndFreeze() {
		return endFreeze;
	}

	public void setEndFreeze(Long endFreeze) {
		this.endFreeze = endFreeze;
	}

	public Long getEndBalance() {
		return endBalance;
	}

	public void setEndBalance(Long endBalance) {
		this.endBalance = endBalance;
	}

	public Long getEndAvailable() {
		return endAvailable;
	}

	public void setEndAvailable(Long endAvailable) {
		this.endAvailable = endAvailable;
	}

	public String getBusinessData() {
		return this.businessData;
	}

	public void setBusinessData(String businessData) {
		this.businessData = businessData;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
