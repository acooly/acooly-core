package com.acooly.core.common.dao.dialect;

import com.acooly.core.common.dao.dialect.impl.H2DatabaseDialect;
import com.acooly.core.common.dao.dialect.impl.MySQLDatabaseDialect;
import com.acooly.core.common.dao.dialect.impl.OracleDatabaseDialect;
import com.acooly.core.common.dao.support.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public final class DatabaseDialectManager {

    private static Map<DatabaseType, DatabaseDialect> dialects = Maps.newHashMap();

    static {
        register(new MySQLDatabaseDialect());
        register(new H2DatabaseDialect());
        register(new OracleDatabaseDialect());
    }

    private DatabaseDialectManager() {
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
        if (StringUtils.contains(jdbcUrl, ":mysql:")) {
            return DatabaseType.mysql;
        } else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
            return DatabaseType.oracle;
        } else if (StringUtils.contains(jdbcUrl, ":h2:")) {
            return DatabaseType.h2;
        } else if (StringUtils.contains(jdbcUrl, ":hsql:")) {
            return DatabaseType.hsql;
        } else if (StringUtils.contains(jdbcUrl, ":postgresql:")) {
            return DatabaseType.postgresql;
        } else if (StringUtils.contains(jdbcUrl, ":sql_server:")) {
            return DatabaseType.sql_server;
        } else if (StringUtils.contains(jdbcUrl, ":sybase:")) {
            return DatabaseType.sybase;
        } else {
            throw new IllegalArgumentException("组件初始化执行SQL Unknown Database of " + jdbcUrl);
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
}
