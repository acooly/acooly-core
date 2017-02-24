package com.acooly.module.app.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.app.dao.AppBannerDao;
import com.acooly.module.app.domain.AppBanner;
import com.acooly.module.app.service.AppBannerService;

@Service("appBannerService")
public class AppBannerServiceImpl extends EntityServiceImpl<AppBanner, AppBannerDao> implements AppBannerService {

}
