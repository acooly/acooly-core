package com.acooly.core.common.dao.dialect.impl;

import com.acooly.core.common.dao.dialect.DatabaseDialect;
import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.PageInfo;

public class MySQLDatabaseDialect implements DatabaseDialect {

	@Override
	public String pageSql(@SuppressWarnings("rawtypes") PageInfo pageInfo, String originalSql) {
		int offset = pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1);
		if (offset < 0) {
			offset = 0;
		}
		String pageSql = originalSql + " limit " + offset + "," + pageInfo.getCountOfCurrentPage();
		return pageSql;
	}

	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.mysql;
	}

}
