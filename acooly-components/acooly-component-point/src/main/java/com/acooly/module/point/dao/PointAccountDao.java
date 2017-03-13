/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.point.domain.PointAccount;

/**
 * 积分账户 Mybatis Dao
 *
 * Date: 2017-02-03 22:45:12
 * 
 * @author cuifuqiang
 */
public interface PointAccountDao extends EntityMybatisDao<PointAccount> {

	@Select("select * from pt_point_account where user_name=#{userName}")
	PointAccount findByUserName(@Param("userName") String userName);

}
