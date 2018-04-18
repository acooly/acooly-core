/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 *
 */
package com.acooly.module.point.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointClearConfig;

import java.util.Date;

/**
 * 积分清零设置 Service接口
 *
 * <p>Date: 2017-04-19 16:24:31
 *
 * @author acooly
 */
public interface PointClearConfigService extends EntityService<PointClearConfig> {

    public long getClearPoint(String userName, Date tradeTime);
}
