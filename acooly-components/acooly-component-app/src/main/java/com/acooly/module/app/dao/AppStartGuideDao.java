package com.acooly.module.app.dao;

import java.util.List;

import com.acooly.module.app.domain.AppStartGuide;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.jpa.EntityJpaDao;

/**
 * app_start_guide JPA Dao
 *
 * Date: 2015-05-22 14:44:16
 *
 * @author Acooly Code Generator
 *
 */
public interface AppStartGuideDao extends EntityJpaDao<AppStartGuide, Long> {
	List<AppStartGuide> findByStatusOrderBySortOrderAsc(EntityStatus status);
}
