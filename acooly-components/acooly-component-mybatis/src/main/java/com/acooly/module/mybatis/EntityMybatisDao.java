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
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qiubo@yiji.com
 */
public interface EntityMybatisDao<T>	extends EntityDao<T>, Mapper<T>, CreateMapper<T>, GetMapper<T>, UpdateMapper<T>,
									FlushMapper<T>, RemoveMapper<T>, SavesMapper<T>, FindMapper<T>, ListMapper<T> {
	
}
