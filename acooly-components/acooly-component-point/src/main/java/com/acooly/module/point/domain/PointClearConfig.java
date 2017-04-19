/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-04-19
*/
package com.acooly.module.point.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.NotNull;

import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;
import com.acooly.module.point.enums.PointClearConfigStatus;

/**
 * 积分清零设置 Entity
 *
 * @author acooly Date: 2017-04-19 16:24:31
 */
@Entity
@Table(name = "point_clear_config")
public class PointClearConfig extends AbstractEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 开始交易时间 */
	private Date startTradeTime;

	/** 结束交易时间 */
	private Date endTradeTime;

	/** 开始清理时间 */
	private Date startClearTime;

	/** 结束清理时间 */
	private Date endClearTime;

	/** 清零时间 */
	private Date clearTime;

	/** 状态 */
	@Enumerated(EnumType.STRING)
	private PointClearConfigStatus status=PointClearConfigStatus.init;

	/** 备注 */
	private String memo;

	public Date getStartTradeTime() {
		return this.startTradeTime;
	}

	public void setStartTradeTime(Date startTradeTime) {
		this.startTradeTime = startTradeTime;
	}

	public Date getEndTradeTime() {
		return this.endTradeTime;
	}

	public void setEndTradeTime(Date endTradeTime) {
		this.endTradeTime = endTradeTime;
	}

	public Date getStartClearTime() {
		return this.startClearTime;
	}

	public void setStartClearTime(Date startClearTime) {
		this.startClearTime = startClearTime;
	}

	public Date getEndClearTime() {
		return this.endClearTime;
	}

	public void setEndClearTime(Date endClearTime) {
		this.endClearTime = endClearTime;
	}

	public Date getClearTime() {
		return this.clearTime;
	}

	public void setClearTime(Date clearTime) {
		this.clearTime = clearTime;
	}

	public PointClearConfigStatus getStatus() {
		return this.status;
	}

	public void setStatus(PointClearConfigStatus status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
