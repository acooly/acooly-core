/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-01
 *
 */
package com.acooly.module.portlet.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.portlet.entity.PortletConfig;

import java.util.List;

/**
 * p_portlet_config Service接口
 * <p>
 * Date: 2017-03-01 00:53:18
 *
 * @author acooly
 */
public interface PortletConfigService extends EntityService<PortletConfig> {


    List<PortletConfig> getConfigByType(String type);

    PortletConfig getConfigByKey(String type, String key);

    PortletConfig getConfigByKey(String key);

}
