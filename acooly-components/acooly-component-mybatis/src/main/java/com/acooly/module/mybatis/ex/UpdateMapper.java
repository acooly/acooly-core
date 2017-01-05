/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:24 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.provider.base.BaseUpdateProvider;

/**
 * @author qiubo@yiji.com
 */
public interface UpdateMapper<T> {
	@UpdateProvider(type = UpdateByPrimaryKeySelectiveProvider.class, method = "dynamicSQL")
	void update(T o);
	
	class UpdateByPrimaryKeySelectiveProvider extends BaseUpdateProvider {
		
		public UpdateByPrimaryKeySelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
			super(mapperClass, mapperHelper);
		}
		
		public String update(MappedStatement ms) {
			return super.updateByPrimaryKeySelective(ms);
		}
	}
}
