/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 17:38 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.dao.EntityDao;
import com.acooly.module.mybatis.ex.*;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Marker;

/**
 * @author qiubo@yiji.com
 */
public interface EntityMybatisDao<T>
        extends EntityDao<T>,
        BaseMapper<T>,
        ExampleMapper<T>,
        Marker,
        CreateMapper<T>,
        GetMapper<T>,
        UpdateMapper<T>,
        FlushMapper<T>,
        RemoveMapper<T>,
        SavesMapper<T>,
        InsertsMapper<T>,
        FindMapper<T>,
        ListMapper<T> {
}
