package com.acooly.core.common.dao.dialect;

import com.acooly.core.common.dao.support.PageInfo;

public interface DatabaseDialect {

    String pageSql(@SuppressWarnings("rawtypes") PageInfo pageInfo, String originalSql);

    DatabaseType getDatabaseType();
}
