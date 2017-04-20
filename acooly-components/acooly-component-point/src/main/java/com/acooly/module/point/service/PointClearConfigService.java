/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 *
 */
package com.acooly.module.point.service;

import java.util.Date;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointClearConfig;

/**
 * 积分清零设置 Service接口
 *
 * Date: 2017-04-19 16:24:31
 * 
 * @author acooly
 *
 */
public interface PointClearConfigService extends EntityService<PointClearConfig> {

	public long getClearPoint(String userName, Date tradeTime);

}
