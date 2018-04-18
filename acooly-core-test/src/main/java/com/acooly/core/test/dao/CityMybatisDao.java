/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 17:53 创建
 */
package com.acooly.core.test.dao;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.test.domain.City;
import com.acooly.module.mybatis.EntityMybatisDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public interface CityMybatisDao extends EntityMybatisDao<City> {
    @Select("select * from City")
    List<City> selectAll();

    /**
     * 分页查询
     *
     * @param pageInfo
     * @return
     */
    @Select("select * from City")
    List<City> selectAllByPage(PageInfo<City> pageInfo);

    @Select("select * from City  where id=#{id} for update")
    City selectById(@Param("id") Long id);

    PageInfo<City> selectAllByPage1(PageInfo pageInfo);
}
