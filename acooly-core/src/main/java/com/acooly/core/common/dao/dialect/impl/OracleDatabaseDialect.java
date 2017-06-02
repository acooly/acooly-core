package com.acooly.core.common.dao.dialect.impl;

import com.acooly.core.common.dao.dialect.DatabaseDialect;
import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.PageInfo;
import org.apache.commons.lang3.StringUtils;

public class OracleDatabaseDialect implements DatabaseDialect {

  private static final String ORACLE_PAGESQL_TEMPLATE =
      "SELECT * FROM (SELECT XX.*,rownum ROW_NUM FROM (${sql}) XX ) ZZ"
          + " where ZZ.ROW_NUM BETWEEN ${startNum} AND ${endNum}";

  @Override
  public String pageSql(@SuppressWarnings("rawtypes") PageInfo pageInfo, String originalSql) {

    String pageSql = ORACLE_PAGESQL_TEMPLATE;
    int startNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1) + 1;
    int endNum = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage());
    if (endNum > pageInfo.getTotalCount()) {
      endNum = (int) pageInfo.getTotalCount();
    }
    pageSql = StringUtils.replace(pageSql, "${sql}", originalSql);
    pageSql = StringUtils.replace(pageSql, "${startNum}", String.valueOf(startNum));
    pageSql = StringUtils.replace(pageSql, "${endNum}", String.valueOf(endNum));

    return pageSql;
  }

  @Override
  public DatabaseType getDatabaseType() {
    return DatabaseType.oracle;
  }
}
