/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-08-14
 *
 */
package com.acooly.component.portlet.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.acooly.component.portlet.enums.ActionChannel;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * 访问日志 Entity
 *
 * @author acooly
 * Date: 2016-08-14 22:10:17
 */
@Entity
@Table(name = "p_action_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActionLog extends AbstractEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** ID */
	@Id	
	@GeneratedValue
	private Long id;
	/** 操作(如：登录，特定功能的访问) */
	private String action;
	/** 用户名 */
	private String userName;
	/** 操作名称 */
	private String actionName;
	/** 渠道 */
	@Enumerated(EnumType.STRING)
	private ActionChannel channel;
	/** 渠道信息 */
	private String channelInfo;
	/** 渠道版本 */
	private String channelVersion;
	/** 客户IP */
	private String userIp;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String comments;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public String getAction(){
		return this.action;
	}
	
	public void setAction(String action){
		this.action = action;
	}
	public ActionChannel getChannel(){
		return this.channel;
	}
	
	public void setChannel(ActionChannel channel){
		this.channel = channel;
	}
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getActionName(){
		return this.actionName;
	}
	
	public void setActionName(String actionName){
		this.actionName = actionName;
	}
	public String getChannelInfo(){
		return this.channelInfo;
	}
	
	public void setChannelInfo(String channelInfo){
		this.channelInfo = channelInfo;
	}
	public String getChannelVersion(){
		return this.channelVersion;
	}
	
	public void setChannelVersion(String channelVersion){
		this.channelVersion = channelVersion;
	}
	public String getUserIp(){
		return this.userIp;
	}
	
	public void setUserIp(String userIp){
		this.userIp = userIp;
	}
	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	public Date getUpdateTime(){
		return this.updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	public String getComments(){
		return this.comments;
	}
	
	public void setComments(String comments){
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
