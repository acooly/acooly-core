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
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseUpdateProvider;

import java.util.Set;

/** @author qiubo@yiji.com */
public interface UpdateMapper<T> {
  @UpdateProvider(type = UpdateByPrimaryKeySelectiveProvider.class, method = "dynamicSQL")
  void update(T o);

  class UpdateByPrimaryKeySelectiveProvider extends BaseUpdateProvider {

    public UpdateByPrimaryKeySelectiveProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
      super(mapperClass, mapperHelper);
    }

    public static String updateSetColumns(
        Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
      StringBuilder sql = new StringBuilder();
      sql.append("<set>");
      //获取全部列
      Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
      //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
      for (EntityColumn column : columnList) {
        if (!column.isId() && column.isUpdatable() && !column.getProperty().equals("createTime")) {
          if (notNull) {
            sql.append(
                SqlHelper.getIfNotNull(
                    entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
          } else {
            sql.append(column.getColumnEqualsHolder(entityName) + ",");
          }
        }
      }
      sql.append("</set>");
      return sql.toString();
    }

    public String update(MappedStatement ms) {
      Class<?> entityClass = getEntityClass(ms);
      StringBuilder sql = new StringBuilder();
      sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
      sql.append(updateSetColumns(entityClass, null, true, isNotEmpty()));
      sql.append(SqlHelper.wherePKColumns(entityClass));
      return sql.toString();
    }
  }
}
