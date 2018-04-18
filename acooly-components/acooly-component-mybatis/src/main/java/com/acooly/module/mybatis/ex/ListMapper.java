/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:24 创建
 */
package com.acooly.module.mybatis.ex;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.dao.support.SearchFilter;
import com.acooly.core.utils.Strings;
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
public interface ListMapper<T> {

    /**
     * 条件排序查询
     *
     * @param map
     * @param sortMap
     * @return
     */
    @SelectProvider(type = ListProvider.class, method = "dynamicSQL")
    List<T> list(Map<String, Object> map, Map<String, Boolean> sortMap);

    @SelectProvider(type = ListProvider.class, method = "dynamicSQL")
    PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap);

    class ListProvider extends MapperTemplate {

        public ListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        public void list(MappedStatement ms) {
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
            ListSqlSource findSqlSource =
                    new ListSqlSource(ms, entityClass, sql.toString(), fieldsTypes, 1);
            setSqlSource(ms, findSqlSource);
        }

        public void query(MappedStatement ms) {
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
            ListSqlSource findSqlSource =
                    new ListSqlSource(ms, entityClass, sql.toString(), fieldsTypes, 2);
            setSqlSource(ms, findSqlSource);
        }
    }

    class ListSqlSource implements SqlSource {
        private MappedStatement ms;
        private String sql;
        private Map<String, Class<?>> fieldsTypes;
        private int index;

        public ListSqlSource(
                MappedStatement ms,
                Class<?> entityClass,
                String sql,
                Map<String, Class<?>> fieldsTypes,
                int index) {
            this.ms = ms;
            this.sql = sql;
            this.fieldsTypes = fieldsTypes;
            this.index = index;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            Assert.isInstanceOf(MapperMethod.ParamMap.class, parameterObject);
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameterObject;
            Map<String, Object> map = (Map<String, Object>) paramMap.get("param" + index);
            Map<String, Boolean> sortMap = (Map<String, Boolean>) paramMap.get("param" + (index + 1));
            StringBuilder sqlResult = new StringBuilder();
            sqlResult.append(sql);
            if (map != null && !map.isEmpty()) {
                sqlResult.append(" WHERE ");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    SearchFilter searchFilter = SearchFilter.parse(entry.getKey(), entry.getValue());
                    if (searchFilter == null) {
                        continue;
                    }
                    Class proType = fieldsTypes.get(searchFilter.fieldName);
                    Assert.notNull(proType, "属性[" + searchFilter.fieldName + "]不存在");
                    sqlResult.append(SearchFilterParser.parseSqlField(searchFilter, proType));
                    sqlResult.append(" AND ");
                }
                sqlResult.delete(sqlResult.length() - 4, sqlResult.length() - 1);
            }
            sqlResult.append("order by ");
            if (sortMap == null || sortMap.isEmpty()) {
                sqlResult.append(" id desc");
            } else {
                for (Map.Entry<String, Boolean> entry : sortMap.entrySet()) {
                    sqlResult.append(
                            Strings.camelToUnderline(entry.getKey())
                                    + "  "
                                    + (entry.getValue() ? "ASC" : "DESC"));
                    sqlResult.append(",");
                }
                sqlResult.deleteCharAt(sqlResult.length() - 1);
            }

            BoundSql boundSql = new BoundSql(ms.getConfiguration(), sqlResult.toString(), null, null);
            return boundSql;
        }
    }
}
