package com.acooly.module.app.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.app.dao.AppWelcomeDao;
import com.acooly.module.app.domain.AppWelcome;
import com.acooly.module.app.service.AppWelcomeService;

@Service("appWelcomeService")
public class AppWelcomeServiceImpl extends EntityServiceImpl<AppWelcome, AppWelcomeDao> implements AppWelcomeService {

	@Override
	public AppWelcome getLatestOne() {
		return getEntityDao().getLatestOne();
	}

}