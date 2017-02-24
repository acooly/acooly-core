package com.acooly.module.app.service;

import java.util.List;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.app.domain.AppStartGuide;

/**
 * app_start_guide Service
 *
 * Date: 2015-05-22 14:44:16
 *
 * @author Acooly Code Generator
 *
 */
public interface AppStartGuideService extends EntityService<AppStartGuide> {
	List<AppStartGuide> loadValidGuides();
}
