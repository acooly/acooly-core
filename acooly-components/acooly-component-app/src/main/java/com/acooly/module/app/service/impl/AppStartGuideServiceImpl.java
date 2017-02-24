package com.acooly.module.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.app.dao.AppStartGuideDao;
import com.acooly.module.app.domain.AppStartGuide;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.app.service.AppStartGuideService;

@Service("appStartGuideService")
public class AppStartGuideServiceImpl extends EntityServiceImpl<AppStartGuide, AppStartGuideDao> implements
		AppStartGuideService {

	@Override
	public List<AppStartGuide> loadValidGuides() {
		return getEntityDao().findByStatusOrderBySortOrderAsc(EntityStatus.Enable);
	}

}
