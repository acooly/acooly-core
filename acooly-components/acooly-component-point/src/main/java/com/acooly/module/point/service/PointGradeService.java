/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 *
 */
package com.acooly.module.point.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointAccount;
import com.acooly.module.point.domain.PointGrade;

/**
 * 积分等级 Service接口
 *
 * Date: 2017-02-03 22:47:28
 * 
 * @author cuifuqiang
 *
 */
public interface PointGradeService extends EntityService<PointGrade> {

	/**
	 * 获取用户积分等级
	 * 
	 * @param point
	 * @return
	 */
	PointGrade getSectionPoint(PointAccount pointAccount);

}
