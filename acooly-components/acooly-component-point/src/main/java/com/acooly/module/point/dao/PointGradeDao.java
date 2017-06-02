/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.point.domain.PointGrade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 积分等级 Mybatis Dao
 *
 * <p>Date: 2017-02-03 22:47:28
 *
 * @author cuifuqiang
 */
public interface PointGradeDao extends EntityMybatisDao<PointGrade> {

  @Select("select * from point_grade where start_point<=#{point} and end_point>=#{point}")
  PointGrade getSectionPoint(@Param("point") Long point);
}
