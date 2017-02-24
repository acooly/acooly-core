package com.acooly.module.app.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.app.service.AppVersionService;
import com.acooly.module.app.dao.AppVersionDao;
import com.acooly.module.app.domain.AppVersion;

@Service("appVersionService")
public class AppVersionServiceImpl extends EntityServiceImpl<AppVersion, AppVersionDao> implements AppVersionService {

	@Override
	public AppVersion getLatest(String appCode, String deviceType) {
		return getEntityDao().getLatest(appCode, deviceType);
	}

}
