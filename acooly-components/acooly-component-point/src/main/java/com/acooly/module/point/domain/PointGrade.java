/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by cuifuqiang
* date:2017-02-03
*/
package com.acooly.module.point.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.acooly.core.common.domain.AbstractEntity;

/**
 * 积分等级 Entity
 *
 * @author cuifuqiang Date: 2017-02-03 22:47:28
 */
@Entity
@Table(name = "point_grade")
public class PointGrade extends AbstractEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** id */
	/** 等级 */
	private Integer num = 1;
	/** 标题 */
	private String title;
	/** 积分区间_开始 */
	private Long startPoint = 0l;
	/** 积分区间_结束 */
	private Long endPoint = 0l;
	/** 图标 */
	private String picture;
	/** 备注 */
	private String memo;

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Long startPoint) {
		this.startPoint = startPoint;
	}

	public Long getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Long endPoint) {
		this.endPoint = endPoint;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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
