/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:20 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;
import java.util.Set;

/**
 * @author qiubo@yiji.com
 */
public interface SavesMapper<T> {
	@SelectProvider(type = SavesProvider.class, method = "dynamicSQL")
	void saves(List<T> entities);
	
	class SavesProvider extends BaseSelectProvider {
		private static String entityName="item";
		private BaseInsertProvider baseInsertProvider = null;

		public SavesProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
			super(mapperClass, mapperHelper);
			baseInsertProvider = new BaseInsertProvider(mapperClass, mapperHelper);
		}

        /**
		 * 生成结果如下：
		 * <pre class="code">
		 *     {@code

		 	   		<foreach item="item" index="index" collection="list" separator=";">
				 <if test="item.id != null">
				 UPDATE city
				 <set>name = #{item.name,javaType=java.lang.String},state = #{item.state,javaType=java.lang.String},</set>
				 <where>
				 AND Id = #{item.id,javaType=java.lang.Integer}</where>
				 </if>
				 <if test="item.id == null">
				 <bind name="id_cache" value="item.id"/>INSERT INTO city
				 <trim prefix="(" suffix=")" suffixOverrides=",">Id,name,state,</trim>
				 <trim prefix="VALUES(" suffix=")" suffixOverrides=",">
				 <if test="id_cache != null">#{item.id_cache,javaType=java.lang.Integer},</if>
				 <if test="id_cache == null">#{item.id,javaType=java.lang.Integer},</if>
				 <if test="item.name != null">#{item.name,javaType=java.lang.String},</if>
				 <if test="item.name == null">#{item.name,javaType=java.lang.String},</if>
				 <if test="item.state != null">#{item.state,javaType=java.lang.String},</if>
				 <if test="item.state == null">#{item.state,javaType=java.lang.String},</if>
				 </trim>
				 </if>
				 </foreach>
			}
		 * </pre>
		 * @param ms
		 * @return
		 */
		public String saves(MappedStatement ms) {
			String insertSql=insert(ms);
			String updateSql=update(ms);
			String sql = "<foreach item=\"item\" index=\"index\" collection=\"list\"\n"	+ "      separator=\";\" >\n"
							+ "      <if test=\"item.id != null\">\n"
							+ updateSql
							+ "      </if>\n"
							+ "      <if test=\"item.id == null\">\n"
							+ insertSql
							+ "      </if>\n" + "</foreach>";
			return sql;
		}
		public String update(MappedStatement ms) {
			Class<?> entityClass = getEntityClass(ms);
			StringBuilder sql = new StringBuilder();
			sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
			sql.append(SqlHelper.updateSetColumns(entityClass, entityName, false, false));
			sql.append(wherePKColumns(entityClass));
			return sql.toString();
		}
		public String wherePKColumns(Class<?> entityClass) {
			StringBuilder sql = new StringBuilder();
			sql.append("<where>");
			//获取全部列
			Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
			//当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
			for (EntityColumn column : columnList) {
				sql.append(" AND " + column.getColumnEqualsHolder(entityName));
			}
			sql.append("</where>");
			return sql.toString();
		}
		public String insert(MappedStatement ms) {

			Class<?> entityClass = getEntityClass(ms);
			StringBuilder sql = new StringBuilder();
			//获取全部列
			Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
			//Identity列只能有一个
			Boolean hasIdentityKey = false;
			//先处理cache或bind节点
			for (EntityColumn column : columnList) {
				if (!column.isInsertable()) {
					continue;
				}
				if (StringUtil.isNotEmpty(column.getSequenceName())) {
				} else if (column.isIdentity()) {
					//这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
					//这是一个bind节点
					sql.append("<bind name=\"id_cache\" value=\"item.id\"/>");
					//如果是Identity列，就需要插入selectKey
					//如果已经存在Identity列，抛出异常
					if (hasIdentityKey) {
						//jdbc类型只需要添加一次
						if (column.getGenerator() != null && column.getGenerator().equals("JDBC")) {
							continue;
						}
						throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
					}
					//插入selectKey
					newSelectKeyMappedStatement(ms, column);
					hasIdentityKey = true;
				} else if (column.isUuid()) {
					//uuid的情况，直接插入bind节点
					sql.append(SqlHelper.getBindValue(column, getUUID()));
				}
			}
			sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
			sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
			sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
			for (EntityColumn column : columnList) {
				if (!column.isInsertable()) {
					continue;
				}
				//优先使用传入的属性值,当原属性property!=null时，用原属性
				//自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
				if (column.isIdentity()) {
					sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(entityName, "_cache", ",")));
				} else {
					//其他情况值仍然存在原property中
					sql.append(SqlHelper.getIfNotNull(entityName,column, column.getColumnHolder(entityName, null, ","), isNotEmpty()));
				}
				//当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
				//序列的情况
				if (StringUtil.isNotEmpty(column.getSequenceName())) {
					sql.append(SqlHelper.getIfIsNull(entityName,column, getSeqNextVal(column) + " ,", false));
				} else if (column.isIdentity()) {
					sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder(entityName) + ","));
				} else if (column.isUuid()) {
					sql.append(SqlHelper.getIfIsNull(entityName,column, column.getColumnHolder(entityName, "_bind", ","), isNotEmpty()));
				} else {
					//当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
					sql.append(SqlHelper.getIfIsNull(entityName,column, column.getColumnHolder(entityName, null, ","), isNotEmpty()));
				}
			}
			sql.append("</trim>");
			return sql.toString();
		}
	}
	
}
