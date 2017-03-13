/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.point.dao.PointGradeDao;
import com.acooly.module.point.domain.PointGrade;
import com.acooly.module.point.service.PointGradeService;

/**
 * 积分等级 Service实现
 *
 * Date: 2017-02-03 22:47:28
 *
 * @author cuifuqiang
 *
 */
@Service("pointGradeService")
public class PointGradeServiceImpl extends EntityServiceImpl<PointGrade, PointGradeDao> implements PointGradeService {

	@Override
	public PointGrade getSectionPoint(Long point) {
		PointGrade pointGrade = getEntityDao().getSectionPoint(point);
		if (pointGrade == null) {
			throw new BusinessException("未找到匹配的积分用户等级");
		}
		return pointGrade;
	}

}
