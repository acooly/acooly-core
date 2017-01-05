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
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseDeleteProvider;

import java.io.Serializable;

/**
 * @author qiubo@yiji.com
 */
public interface RemoveMapper<T> {

	@UpdateProvider(type = RemoveProvider.class, method = "dynamicSQL")
	void remove(T o);
	
	/**
	 * 根据ID删除
	 *
	 * @param id
	 */
	@UpdateProvider(type = RemoveProvider.class, method = "dynamicSQL")
	void removeById(Serializable id);
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	@UpdateProvider(type = RemoveProvider.class, method = "dynamicSQL")
	void removes(Serializable... ids);
	
	class RemoveProvider extends BaseDeleteProvider {
		
		public RemoveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
			super(mapperClass, mapperHelper);
		}
		
		public String removeById(MappedStatement ms) {
			return super.deleteByPrimaryKey(ms);
		}
		public String remove(MappedStatement ms) {
			return super.delete(ms);
		}
		public String removes(MappedStatement ms) {
			Class<?> entityClass = getEntityClass(ms);
			StringBuilder sql = new StringBuilder();
			sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
			sql.append(" WHERE ID IN");
			sql.append("<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">  \n" +
					" #{item}  \n" +
					"</foreach>  ");
			return sql.toString();
		}
	}
}
