/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-01
 */
package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.dao.PortletConfigDao;
import com.acooly.module.portlet.entity.PortletConfig;
import com.acooly.module.portlet.service.PortletConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * p_portlet_config Service实现
 * <p>
 * Date: 2017-03-01 00:53:18
 *
 * @author acooly
 */
@Service("portletConfigService")
public class PortletConfigServiceImpl extends EntityServiceImpl<PortletConfig, PortletConfigDao> implements PortletConfigService {


    @Override
    public List<PortletConfig> getConfigByType(String type) {
        return null;
    }

    @Override
    public PortletConfig getConfigByKey(String type, String key) {
        return null;
    }

    @Override
    public PortletConfig getConfigByKey(String key) {
        return null;
    }
}
