package com.acooly.module.app.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.app.service.AppCrashService;
import com.acooly.module.app.dao.AppCrashDao;
import com.acooly.module.app.domain.AppCrash;

@Service("appCrashService")
public class AppCrashServiceImpl extends EntityServiceImpl<AppCrash, AppCrashDao> implements AppCrashService {

}
