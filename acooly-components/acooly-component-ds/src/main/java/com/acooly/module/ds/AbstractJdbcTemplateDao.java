/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月29日
 *
 */
package com.acooly.module.ds;

import com.acooly.core.common.dao.support.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基于native-sql的DAO基类
 *
 * @author zhangpu
 */
public abstract class AbstractJdbcTemplateDao {

  private static final String ORACLE_PAGESQL_TEMPLATE =
      "SELECT * FROM (SELECT XX.*,rownum ROW_NUM FROM (${sql}) XX ) ZZ"
          + " where ZZ.ROW_NUM BETWEEN ${startNum} AND ${endNum}";
  @Autowired protected JdbcTemplate jdbcTemplate;

  private DbType dbType;

  public static String getJdbcUrlFromDataSource(DataSource dataSource) {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      if (connection == null) {
        throw new IllegalStateException(
            "Connection returned by DataSource [" + dataSource + "] was null");
      }
      return connection.getMetaData().getURL();
    } catch (SQLException e) {
      throw new RuntimeException("Could not get database url", e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public <T> PageInfo<T> query(PageInfo<T> pageInfo, String sql, Class<T> requiredType) {
    DbType dbType = getDbType();
    if (dbType == DbType.mysql) {
      return queryMySql(pageInfo, sql, requiredType);
    } else if (dbType == DbType.oracle) {
      return queryOracle(pageInfo, sql, requiredType);
    } else {
      throw new UnsupportedOperationException("不支持[" + dbType + "]的分页查询");
    }
  }

  protected DbType getDbType() {
    if (dbType == null) {
      synchronized (this) {
        if (dbType == null) {
          String jdbcUrl = getJdbcUrlFromDataSource(jdbcTemplate.getDataSource());
          if (jdbcUrl.contains(":mysql:")) {
            dbType = DbType.mysql;
          } else if (jdbcUrl.contains(":oracle:")) {
            dbType = DbType.oracle;
          } else {
            // 暂时设置默认mysql
            dbType = DbType.mysql;
          }
        }
      }
    }
    return dbType;
  }

  /**
   * 实体查询
   *
   * @param sql 查询sql
   * @param elementType 实体类型 支持pojo或map
   * @return List<elementType>
   * @throws DataAccessException
   */
  @SuppressWarnings("unchecked")
  protected <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
    if (elementType.isAssignableFrom(Map.class)) {
      return (List<T>) jdbcTemplate.queryForList(sql);
    } else {
      return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(elementType));
    }
  }

  /**
   * Oracle jdbc 分页查询简单封装
   *
   * @param <T>
   * @param pageInfo
   * @param sql
   * @param requiredType
   * @return
   */
  private <T> PageInfo<T> queryOracle(PageInfo<T> pageInfo, String sql, Class<T> requiredType) {
    String orderBy = "";
    if (StringUtils.contains(sql, "order by")) {
      orderBy = sql.substring(sql.indexOf("order by"));
      sql = sql.substring(0, sql.indexOf("order by"));
    }
    String sqlCount = "select count(*) from (" + sql + ") as xxttbb";
    // 总记录数
    long totalCount = getCount(sqlCount);
    long totalPage =
        (totalCount % pageInfo.getCountOfCurrentPage() == 0
            ? totalCount / pageInfo.getCountOfCurrentPage()
            : totalCount / pageInfo.getCountOfCurrentPage() + 1);
    pageInfo.setTotalCount(totalCount);
    pageInfo.setTotalPage(totalPage);

    String pageSql = ORACLE_PAGESQL_TEMPLATE + " " + orderBy;
    int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1) + 1;
    int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
    if (endNum > totalCount) {
      endNum = (int) totalCount;
    }
    pageSql = StringUtils.replace(pageSql, "${sql}", sql);
    pageSql = StringUtils.replace(pageSql, "${startNum}", String.valueOf(startNum));
    pageSql = StringUtils.replace(pageSql, "${endNum}", String.valueOf(endNum));

    List<T> result = queryForList(pageSql, requiredType);

    pageInfo.setPageResults(result);

    return pageInfo;
  }

  /**
   * mysql 分页查询
   *
   * @param pageInfo
   * @param sql
   * @param dtoEntity
   * @return
   */
  private <T> PageInfo<T> queryMySql(PageInfo<T> pageInfo, String sql, Class<T> dtoEntity) {
    try {
      String sqlCount = "select count(*) from (" + sql + ") as xxttbb";
      // 总记录数
      long totalCount = getCount(sqlCount);
      int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1);
      int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
      if (endNum > totalCount) {
        endNum = (int) totalCount;
      }
      long totalPage =
          (totalCount % pageInfo.getCountOfCurrentPage() == 0
              ? totalCount / pageInfo.getCountOfCurrentPage()
              : totalCount / pageInfo.getCountOfCurrentPage() + 1);

      String pageSql = sql + " limit " + startNum + "," + pageInfo.getCountOfCurrentPage();
      List<T> result = queryForList(pageSql, dtoEntity);
      pageInfo.setPageResults(result);
      pageInfo.setTotalCount(totalCount);
      pageInfo.setTotalPage(totalPage);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return pageInfo;
  }

  private long getCount(String sqlCount) {
    return jdbcTemplate.queryForObject(sqlCount, Long.class);
  }

  public static enum DbType {
    mysql,
    oracle
  }
}
