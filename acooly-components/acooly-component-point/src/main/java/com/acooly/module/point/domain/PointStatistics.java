/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-03-13
*/
package com.acooly.module.point.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.point.enums.PointStaticsStatus;

/**
 * 积分统计 Entity
 *
 * @author acooly Date: 2017-03-13 11:51:10
 */
@Entity
@Table(name = "point_statistics")
public class PointStatistics extends AbstractEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 用户名 */
	private String userName;

	/** 统计条数 */
	private Long num;

	/** 统计积分 */
	private Long point = 0l;

	/** 真实处理积分 */
	private Long actualPoint = 0l;

	/** 开始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;

	/** 状态 */
	@Enumerated(EnumType.STRING)
	private PointStaticsStatus status;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date updateTime;

	/** 备注 */
	private String memo;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getNum() {
		return this.num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Long getPoint() {
		return this.point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getActualPoint() {
		return actualPoint;
	}

	public void setActualPoint(Long actualPoint) {
		this.actualPoint = actualPoint;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public PointStaticsStatus getStatus() {
		return this.status;
	}

	public void setStatus(PointStaticsStatus status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
