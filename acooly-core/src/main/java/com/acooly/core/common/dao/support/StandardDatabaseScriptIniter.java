/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 17:24 创建
 */
package com.acooly.core.common.dao.support;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.Env;
import com.acooly.core.common.dao.dialect.DatabaseDialectManager;
import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.exception.AppConfigException;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库初始化组件： 1. 仅在非online环境执行 2. 他判断数据库存在与否，不存在执行sql文件
 *
 * @author qiubo@yiji.com
 */
public abstract class StandardDatabaseScriptIniter
        implements ApplicationListener<DataSourceReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(StandardDatabaseScriptIniter.class);
    private static final String EVALUATE_SQL_PATTERN = "SELECT count(*) FROM %s";
    /**
     * 数据库脚本路径，第一个%s为组件名，第二个%s为数据库名，第三个为脚本文件名
     */
    private static final String INIT_SQL_PATTERN = "META-INF/database/%s/%s/%s.sql";

    @Override
    public void onApplicationEvent(DataSourceReadyEvent event) {
        if (Env.isOnline()
                || !Apps.getEnvironment()
                .getProperty("acooly.ds.autoCreateTable", Boolean.class, Boolean.TRUE)) {
            return;
        }
        DataSource dataSource = (DataSource) event.getSource();
        try {
            DatabaseType databaseType =
                    DatabaseDialectManager.getDatabaseType(dataSource.getConnection());
            String componentName = getComponentName();
            String evaluateTable = getEvaluateTable();
            Assert.notNull(componentName);
            Assert.notNull(evaluateTable);
            if (!"SYS_USER".equals(evaluateTable)) {
                Assert.isTrue(evaluateTable.contains(componentName));
            }
            String evaluateSql = String.format(EVALUATE_SQL_PATTERN, evaluateTable);
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                ScriptUtils.executeSqlScript(connection, new ByteArrayResource(evaluateSql.getBytes()));
            } catch (DataAccessException e) {
                Throwable throwable = Throwables.getRootCause(e);
                String msg = throwable.getMessage();
                if (throwable.getClass().getName().endsWith("MySQLSyntaxErrorException")
                        && msg.endsWith("doesn't exist")) {
                    List<String> files = getInitSqlFile();
                    List<String> abFiles = Lists.newArrayListWithCapacity(files.size());
                    for (String file : files) {
                        String initSql =
                                String.format(
                                        INIT_SQL_PATTERN, componentName, databaseType.name().toLowerCase(), file);
                        abFiles.add(initSql);
                    }
                    if (connection != null) {
                        Connection conn = connection;
                        abFiles.forEach(sqlPath -> exeSqlFile(componentName, conn, sqlPath));
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new AppConfigException(e);
        }
    }

    /**
     * 判断表是否存在,表名必须已组件或模块名称开头
     */
    public abstract String getEvaluateTable();

    /**
     * 组件或应用模块名称
     */
    public abstract String getComponentName();

    /**
     * 数据库初始化脚本文件名
     */
    public abstract List<String> getInitSqlFile();

    private void exeSqlFile(String componentName, Connection connection, String sqlpath) {
        logger.info("发现组件或模块[{}]基础数据还没有初始化，开始初始化:{}", componentName, sqlpath);
        try {
            Resource scriptResource = ApplicationContextHolder.get().getResource("classpath:" + sqlpath);
            EncodedResource encodedResource = new EncodedResource(scriptResource, Charsets.UTF_8);
            ScriptUtils.executeSqlScript(connection, encodedResource);
        } catch (Exception e) {
            throw new AppConfigException("初始化" + componentName + "失败", e);
        }
    }
}
