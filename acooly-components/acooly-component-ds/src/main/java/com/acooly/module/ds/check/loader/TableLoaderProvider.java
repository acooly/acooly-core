package com.acooly.module.ds.check.loader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.acooly.core.common.dao.dialect.DatabaseDialectManager.getJdbcUrlFromDataSource;

/**
 * @author shuijing
 */
public class TableLoaderProvider {
    private static Logger logger = LoggerFactory.getLogger(TableLoaderProvider.class);

    public static TableLoader getTableLoader(DataSource dataSource) throws SQLException {
        TableLoader tableLoader;
        String jdbcUrl = getJdbcUrlFromDataSource(dataSource.getConnection());
        if (StringUtils.contains(jdbcUrl, ":mysql:")) {
            tableLoader = new MysqlTableLoader(dataSource);
        } else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
            tableLoader = new OracleTableLoader(dataSource);
        } else {
            throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
        }
        return tableLoader;
    }
}
