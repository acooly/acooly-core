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
 * @author xiyang
 */
public interface InsertsMapper<T> {
    @SelectProvider(type = InsertsProvider.class, method = "dynamicSQL")
    void inserts(List<T> entities);

    class InsertsProvider extends BaseSelectProvider {
        private static String entityName = "item";
        private BaseInsertProvider baseInsertProvider = null;

        public InsertsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
            baseInsertProvider = new BaseInsertProvider(mapperClass, mapperHelper);
        }

        /**
         * 等待修复<if test="id_cache != null">#{item.id_cache,javaType=java.lang.Integer},</if>
         * item.id_cache是获取不到值的，因为绑定项为id_cache，但经实际测试foreach中使用bing来缓存id，始终id为同一个，无法按预期的循环
         * 顾修改为直接获取item.id
         * 生成结果如下：
         *
         * <pre class="code">{@code
         * <foreach item="item" index="index" collection="list" separator=";">
         * <if test="item.id != null">
         * UPDATE city
         * <set>name = #{item.name,javaType=java.lang.String},state = #{item.state,javaType=java.lang.String},</set>
         * <where>
         * AND Id = #{item.id,javaType=java.lang.Integer}</where>
         * </if>
         * <if test="item.id == null">
         * <bind name="id_cache" value="item.id"/>INSERT INTO city
         * <trim prefix="(" suffix=")" suffixOverrides=",">Id,name,state,</trim>
         * <trim prefix="VALUES(" suffix=")" suffixOverrides=",">
         * <if test="id_cache != null">#{item.id_cache,javaType=java.lang.Integer},</if>
         * <if test="id_cache == null">#{item.id,javaType=java.lang.Integer},</if>
         * <if test="item.name != null">#{item.name,javaType=java.lang.String},</if>
         * <if test="item.name == null">#{item.name,javaType=java.lang.String},</if>
         * <if test="item.state != null">#{item.state,javaType=java.lang.String},</if>
         * <if test="item.state == null">#{item.state,javaType=java.lang.String},</if>
         * </trim>
         * </if>
         * </foreach>
         * }</pre>
         *
         * @param ms
         * @return
         */
        public String inserts(MappedStatement ms) {
            String insertSql = insert(ms);
            String sql =
                    "<foreach item=\"item\" index=\"index\" collection=\"list\"\n"
                            + "      separator=\";\" >\n"
                            + insertSql
                            + "</foreach>";
            return sql;
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
                    //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长(?疑问，去掉该bind值直接使用也无任何问题 by夕阳 2019-08-01)
                    //这是一个bind节点
                    sql.append("<bind name=\"id_cache\" value=\"item.id\"/>");
                    //如果是Identity列，就需要插入selectKey
                    //如果已经存在Identity列，抛出异常
                    if (hasIdentityKey) {
                        //jdbc类型只需要添加一次
                        if (column.getGenerator() != null && column.getGenerator().equals("JDBC")) {
                            continue;
                        }
                        throw new RuntimeException(
                                ms.getId()
                                        + "对应的实体类"
                                        + entityClass.getCanonicalName()
                                        + "中包含多个MySql的自动增长列,最多只能有一个!");
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
                    //等待修复<if test="id_cache != null">#{item.id_cache,javaType=java.lang.Integer},</if>
                    //item.id_cache是获取不到值的，因为绑定项为id_cache，但经实际测试foreach中使用bing来缓存id，始终id为同一个，无法按预期的循环
                    // 顾修改为直接获取item.id,修改前原代码逻辑为：
                    //sql.append(
                    //                            SqlHelper.getIfCacheNotNull(
                    //                                    column, column.getColumnHolder(entityName, "_cache", ",")));
                    sql.append(
                            SqlHelper.getIfCacheNotNull(
                                    column, column.getColumnHolder(entityName, null, ",")));
                } else {
                    //其他情况值仍然存在原property中
                    sql.append(
                            SqlHelper.getIfNotNull(
                                    entityName, column, column.getColumnHolder(entityName, null, ","), isNotEmpty()));
                }
                //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
                //序列的情况
                if (StringUtil.isNotEmpty(column.getSequenceName())) {
                    sql.append(
                            SqlHelper.getIfIsNull(entityName, column, getSeqNextVal(column) + " ,", false));
                } else if (column.isIdentity()) {
                    sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder(entityName) + ","));
                } else if (column.isUuid()) {
                    sql.append(
                            SqlHelper.getIfIsNull(
                                    entityName,
                                    column,
                                    column.getColumnHolder(entityName, "_bind", ","),
                                    isNotEmpty()));
                } else {
                    //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                    sql.append(
                            SqlHelper.getIfIsNull(
                                    entityName, column, column.getColumnHolder(entityName, null, ","), isNotEmpty()));
                }
            }
            sql.append("</trim>");
            return sql.toString();
        }
    }
}
