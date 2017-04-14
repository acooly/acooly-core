/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-21
 */
package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.dao.ActionMappingDao;
import com.acooly.module.portlet.entity.ActionMapping;
import com.acooly.module.portlet.service.ActionMappingService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 访问映射 Service实现
 * <p>
 * Date: 2017-03-21 00:34:47
 *
 * @author acooly
 */
@Service("actionMappingService")
public class ActionMappingServiceImpl extends EntityServiceImpl<ActionMapping, ActionMappingDao> implements ActionMappingService {

    @Override
    @Cacheable(value = "actionMapping", key = "#url")
    public ActionMapping getActionMapping(String url) {

        return null;//getEntityDao().findByUrl(url);
    }

}
