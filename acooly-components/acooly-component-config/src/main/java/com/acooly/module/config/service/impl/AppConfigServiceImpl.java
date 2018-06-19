/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by shuijing
 * date:2018-06-19
 */
package com.acooly.module.config.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.config.dao.AppConfigDao;
import com.acooly.module.config.entity.AppConfig;
import com.acooly.module.config.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * sys_app_config Service实现
 * <p>
 * Date: 2018-06-19 21:52:29
 *
 * @author shuijing
 */
@Service("appConfigService")
public class AppConfigServiceImpl extends EntityServiceImpl<AppConfig, AppConfigDao> implements AppConfigService {

    @Autowired
    private AppConfigManager appConfigCache;

    @Override
    public void saveOrUpdate(AppConfig appConfig) throws BusinessException {
        super.saveOrUpdate(appConfig);
        appConfigCache.invalidate(appConfig);
    }


}
