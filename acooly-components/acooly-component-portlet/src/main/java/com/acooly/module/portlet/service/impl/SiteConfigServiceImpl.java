/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-20
 */
package com.acooly.module.portlet.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.service.SiteConfigService;
import com.acooly.module.portlet.dao.SiteConfigDao;
import com.acooly.module.portlet.entity.SiteConfig;

/**
 * portlet_site_config Service实现
 *
 * Date: 2017-03-20 23:36:29
 *
 * @author acooly
 *
 */
@Service("siteConfigService")
public class SiteConfigServiceImpl extends EntityServiceImpl<SiteConfig, SiteConfigDao> implements SiteConfigService {

}
