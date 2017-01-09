/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-21 20:28 创建
 */
package com.acooly.module.ds;

/**
 * @author qiubo
 */

import com.acooly.core.common.dao.support.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * JdbcTemplate分页查询扩展
 *
 * 废弃原来的低版本的ObjectRowMapper扩展
 * 查询结果通过RowMapper直接映射为对象(映射基本规则使用查询的列名与对象的属性名匹配，并实现自动类型转换)
 *
 * 为了简单，如果是复杂多表关联查询，请使用视图。
 *
 * @author zhangpu
 * @author zhangpu 废弃扩展，回归原生，使用聚合代替继承。 2016-03-29
 * @see AbstractJdbcTemplateDao
 */
@Deprecated
@Service("pagedJdbcTemplate")
public class PagedJdbcTemplate extends JdbcTemplate {
    public PagedJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    /** ORACLE 分页查询SQL模版 */
    private static final String ORACLE_PAGESQL_TEMPLATE = "SELECT * FROM (SELECT XX.*,rownum ROW_NUM FROM (${sql}) XX ) ZZ where ZZ.ROW_NUM BETWEEN ${startNum} AND ${endNum}";
    /**
     * Oracle jdbc 分页查询简单封装
     *
     * @param <T>
     * @param pageInfo
     * @param sql
     * @param requiredType
     * @return
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public <T> PageInfo<T> queryOracle(PageInfo<T> pageInfo, String sql, Class<T> requiredType) {
        String orderBy = "";
        if (StringUtils.contains(sql, "order by")) {
            orderBy = sql.substring(sql.indexOf("order by"));
            sql = sql.substring(0, sql.indexOf("order by"));
        }
        String sqlFrom = sql.substring(sql.indexOf("from"));
        String sqlCount = "select count(*) " + sqlFrom;
        // 总记录数
        long totalCount = super.queryForObject(sqlCount,Long.class);
        int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1) + 1;
        int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
        if (endNum > totalCount) {
            endNum = (int) totalCount;
        }
        long totalPage = (totalCount % pageInfo.getCountOfCurrentPage() == 0
                ? totalCount / pageInfo.getCountOfCurrentPage() : totalCount / pageInfo.getCountOfCurrentPage() + 1);
        String pageSql = ORACLE_PAGESQL_TEMPLATE + " " + orderBy;
        pageSql = StringUtils.replace(pageSql, "${sql}", sql);
        pageSql = StringUtils.replace(pageSql, "${startNum}", String.valueOf(startNum));
        pageSql = StringUtils.replace(pageSql, "${endNum}", String.valueOf(endNum));
        List<T> result = null;
        if (requiredType.getName().equals(Map.class.getName())) {
            result = (List<T>) queryForList(pageSql);
        } else {
            result = query(pageSql, BeanPropertyRowMapper.newInstance(requiredType));
        }
        pageInfo.setPageResults(result);
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalPage);
        return pageInfo;
    }
    /**
     * JDBC分页查询
     *
     * @param pageInfo
     * @param sql
     * @param pageColumn
     *            分页用的列(大数据查询时该列最好有索引)
     * @return
     */
    @Deprecated
    public <T> PageInfo<T> queryMSSQL(PageInfo<T> pageInfo, String sql, Class<T> dtoEntity, String pageColumn) {
        try {
            String sqlCount = "select count(*) from (" + sql + ") as t1";
            // 总记录数
            long totalCount = super.queryForObject(sqlCount,Long.class);
            int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1);
            int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
            if (endNum > totalCount) {
                endNum = (int) totalCount;
            }
            long totalPage = (totalCount % pageInfo.getCountOfCurrentPage() == 0
                    ? totalCount / pageInfo.getCountOfCurrentPage()
                    : totalCount / pageInfo.getCountOfCurrentPage() + 1);
            String pageSql = sql + " limit " + startNum + "," + pageInfo.getCountOfCurrentPage();
            List<T> result = query(pageSql, BeanPropertyRowMapper.newInstance(dtoEntity));
            pageInfo.setPageResults(result);
            pageInfo.setTotalCount(totalCount);
            pageInfo.setTotalPage(totalPage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pageInfo;
    }
    /**
     * JDBC分页查询
     *
     * @param pageInfo
     * @param sql
     * @param dto
     * @return
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public <T> PageInfo<T> queryMySql(PageInfo<T> pageInfo, String sql, Class<T> dtoEntity) {
        try {
            String sqlCount = "select count(*) from (" + sql + ") as xxttbb";
            // 总记录数
            long totalCount = super.queryForObject(sqlCount,Long.class);
            int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1);
            int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
            if (endNum > totalCount) {
                endNum = (int) totalCount;
            }
            long totalPage = (totalCount % pageInfo.getCountOfCurrentPage() == 0
                    ? totalCount / pageInfo.getCountOfCurrentPage()
                    : totalCount / pageInfo.getCountOfCurrentPage() + 1);
            String pageSql = sql + " limit " + startNum + "," + pageInfo.getCountOfCurrentPage();
            List<T> result = null;
            if (dtoEntity.getName().equals(Map.class.getName())) {
                result = (List<T>) queryForList(pageSql);
            } else {
                result = query(pageSql, BeanPropertyRowMapper.newInstance(dtoEntity));
            }
            pageInfo.setPageResults(result);
            pageInfo.setTotalCount(totalCount);
            pageInfo.setTotalPage(totalPage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pageInfo;
    }
    /**
     * list 查询
     */
    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        return query(sql, BeanPropertyRowMapper.newInstance(elementType));
    }
}
