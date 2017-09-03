/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.module.store.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品信息 Mybatis Dao
 * <p>
 * Date: 2017-08-21 01:56:35
 *
 * @author zhangpu
 */
public interface GoodsDao extends EntityMybatisDao<Goods> {


    /**
     * 查询指定仓库的所有分类
     *
     * @param storeCode
     * @return
     */
    @Select("select * from st_goods where store_code = #{storeCode} and stock > 0")
    List<Goods> findByStoreCode(@Param("storeCode") String storeCode);
}
