/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-17 18:54
 */
package com.acooly.module.jpa.ex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.orm.jpa.vendor.Database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * 根据jdbc链接字符串诊断是那种常见数据库
 * Copy from org.springframework.boot.autoconfigure.orm.jpa
 *
 * @author zhangpu
 * @date 2022-09-17 18:54
 */
@Slf4j
public class DatabaseLookup {

    private static final Map<DatabaseDriver, Database> LOOKUP;

    static {
        Map<DatabaseDriver, Database> map = new EnumMap<>(DatabaseDriver.class);
        map.put(DatabaseDriver.DERBY, Database.DERBY);
        map.put(DatabaseDriver.H2, Database.H2);
        map.put(DatabaseDriver.HSQLDB, Database.HSQL);
        map.put(DatabaseDriver.MYSQL, Database.MYSQL);
        map.put(DatabaseDriver.ORACLE, Database.ORACLE);
        map.put(DatabaseDriver.POSTGRESQL, Database.POSTGRESQL);
        map.put(DatabaseDriver.SQLSERVER, Database.SQL_SERVER);
        map.put(DatabaseDriver.DB2, Database.DB2);
        map.put(DatabaseDriver.INFORMIX, Database.INFORMIX);
        map.put(DatabaseDriver.HANA, Database.HANA);
        LOOKUP = Collections.unmodifiableMap(map);
    }

    public static Database getDatabase(Connection connection) {
        if (connection == null) {
            return Database.DEFAULT;
        }
        try {
            String url = connection.getMetaData().getURL();
            DatabaseDriver driver = DatabaseDriver.fromJdbcUrl(url);
            Database database = LOOKUP.get(driver);
            if (database != null) {
                return database;
            }
        } catch (Exception ex) {
            log.warn("Unable to determine database from connection", ex);
        }
        return Database.DEFAULT;
    }

    public static Database getDatabase(DataSource dataSource) {
        if (dataSource == null) {
            return Database.DEFAULT;
        }
        try {
            String url = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getURL);
            DatabaseDriver driver = DatabaseDriver.fromJdbcUrl(url);
            Database database = LOOKUP.get(driver);
            if (database != null) {
                return database;
            }
        } catch (MetaDataAccessException ex) {
            log.warn("Unable to determine jdbc url from datasource", ex);
        }
        return Database.DEFAULT;
    }

}
