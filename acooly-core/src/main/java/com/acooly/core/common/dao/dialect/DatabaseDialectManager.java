package com.acooly.core.common.dao.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.acooly.core.common.dao.dialect.impl.MySQLDatabaseDialect;
import com.acooly.core.common.dao.dialect.impl.OracleDatabaseDialect;
import com.acooly.core.common.dao.support.PageInfo;
import com.google.common.collect.Maps;

public final class DatabaseDialectManager {

	private static Map<DatabaseType, DatabaseDialect> dialects = Maps.newHashMap();
	static {
		register(new MySQLDatabaseDialect());
		register(new OracleDatabaseDialect());
	}

	/**
	 * 分页SQL解析
	 * 
	 * @param connection
	 * @param pageInfo
	 * @param sql
	 * @return
	 */
	public static String pageSql(Connection connection, PageInfo pageInfo, String sql) {
		DatabaseType dbType = getDatabaseType(connection);
		return dialects.get(dbType).pageSql(pageInfo, sql);
	}

	/**
	 * 根据数据库连接判断数据库类型
	 * 
	 * @param connection
	 * @return
	 */
	public static DatabaseType getDatabaseType(Connection connection) {
		String jdbcUrl = getJdbcUrlFromDataSource(connection);
		// 根据jdbc url判断dialect
		if (StringUtils.contains(jdbcUrl, ":mysql:")) {
			return DatabaseType.mysql;
		} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
			return DatabaseType.oracle;
		} else {
			throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
		}
	}

	public static String getJdbcUrlFromDataSource(Connection connection) {
		try {
			if (connection == null) {
				throw new IllegalStateException("Connection was null");
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

	private static void register(DatabaseDialect dialect) {
		dialects.put(dialect.getDatabaseType(), dialect);
	}

	private DatabaseDialectManager() {
	}

}
