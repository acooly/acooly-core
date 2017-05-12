package com.acooly.module.point.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 积分交易dto
 * 
 * @author cuifuqiang
 *
 */
public class PointTradeDto {

	/** 相关业务Id **/
	private String busiId;

	/** 相关业务类型 **/
	private String busiType;

	/** 相关业务类型描述 **/
	private String busiTypeText;

	/** 相关业务数据 **/
	private String busiData;

	/** 备注 **/
	private String memo;

	public String getBusiId() {
		return busiId;
	}

	public void setBusiId(String busiId) {
		this.busiId = busiId;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getBusiTypeText() {
		return busiTypeText;
	}

	public void setBusiTypeText(String busiTypeText) {
		this.busiTypeText = busiTypeText;
	}

	public String getBusiData() {
		return busiData;
	}

	public void setBusiData(String busiData) {
		this.busiData = busiData;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
