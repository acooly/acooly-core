/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.store.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品类信息 Mybatis Dao
 * <p>
 * Date: 2017-08-21 01:56:35
 *
 * @author zhangpu
 */
public interface CategoryDao extends EntityMybatisDao<Category> {

    /**
     * 查询指定仓库的所有分类
     *
     * @param storeCode
     * @return
     */
    @Select("select * from st_category where store_code = #{storeCode}")
    List<Category> findByStoreCode(@Param("storeCode") String storeCode);


}
