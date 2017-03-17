/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-08-14
 *
 */
package com.acooly.module.portlet.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.portlet.enums.ActionChannel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 访问日志 Entity
 *
 * @author acooly Date: 2016-08-14 22:10:17
 */
@Table(name = "portlet_action_log")
@Getter
@Setter
public class ActionLog extends AbstractEntity {
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
}
