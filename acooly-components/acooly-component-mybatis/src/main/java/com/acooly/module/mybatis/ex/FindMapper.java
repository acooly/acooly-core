/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:24 创建
 */
package com.acooly.module.mybatis.ex;

import com.acooly.core.common.dao.jpa.SearchFilter;
import com.acooly.module.mybatis.SearchFilterParser;
import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public interface FindMapper<T> {
	@SelectProvider(type = FindProvider.class, method = "dynamicSQL")
	List<T> find(String property, Object value);
	
	@SelectProvider(type = FindProvider.class, method = "dynamicSQL")
	T findUniqu(String property, Object value);
	
	class FindProvider extends MapperTemplate {
		
		public FindProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
			super(mapperClass, mapperHelper);
		}
		
		public void find(MappedStatement ms) {
			final Class<?> entityClass = getEntityClass(ms);
			//将返回值修改为实体类型
			setResultType(ms, entityClass);
			StringBuilder sql = new StringBuilder();
			sql.append(SqlHelper.selectAllColumns(entityClass));
			sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
			Map<String, Class<?>> fieldsTypes = Maps.newHashMap();
			EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
			for (EntityColumn column : entityTable.getEntityClassColumns()) {
				fieldsTypes.put(column.getProperty(), column.getJavaType());
			}
			FindSqlSource findSqlSource = new FindSqlSource(ms, entityClass, sql.toString(), fieldsTypes, false);
			setSqlSource(ms, findSqlSource);
		}
		
		public void findUniqu(MappedStatement ms) {
			final Class<?> entityClass = getEntityClass(ms);
			//将返回值修改为实体类型
			setResultType(ms, entityClass);
			StringBuilder sql = new StringBuilder();
			sql.append(SqlHelper.selectAllColumns(entityClass));
			sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
			Map<String, Class<?>> fieldsTypes = Maps.newHashMap();
			EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
			for (EntityColumn column : entityTable.getEntityClassColumns()) {
				fieldsTypes.put(column.getProperty(), column.getJavaType());
			}
			FindSqlSource findSqlSource = new FindSqlSource(ms, entityClass, sql.toString(), fieldsTypes, true);
			setSqlSource(ms, findSqlSource);
		}
	}
	
	class FindSqlSource implements SqlSource {
		private MappedStatement ms;
		private Class<?> entityClass;
		private String sql;
		private Map<String, Class<?>> fieldsTypes;
		private boolean unique;
		
		public FindSqlSource(	MappedStatement ms, Class<?> entityClass, String sql, Map<String, Class<?>> fieldsTypes,
								boolean unique) {
			this.ms = ms;
			this.entityClass = entityClass;
			this.sql = sql;
			this.fieldsTypes = fieldsTypes;
			this.unique = unique;
		}
		
		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			Assert.isInstanceOf(MapperMethod.ParamMap.class, parameterObject);
			MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
			String property = (String) paramMap.get("param1");
			SearchFilter searchFilter = SearchFilter.parse(property, paramMap.get("param2"));
			StringBuilder sqlResult = new StringBuilder();
			sqlResult.append(sql);
			sqlResult.append(" WHERE ");
			Class proType = fieldsTypes.get(searchFilter.fieldName);
			Assert.notNull(proType, "属性[" + searchFilter.fieldName + "]不存在");
			sqlResult.append(SearchFilterParser.parseSqlField(searchFilter, proType));
			if(unique){
				sqlResult.append(" limit 1");
			}
			BoundSql boundSql = new BoundSql(ms.getConfiguration(), sqlResult.toString(), null, null);
			return boundSql;
		}
	}
}
