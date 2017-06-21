/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-15 18:08 创建
 */
package com.acooly.core.common.dao.support;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.Env;
import com.acooly.core.common.dao.dialect.DatabaseDialectManager;
import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.exception.AppConfigException;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/** @author qiubo@yiji.com */
@Order(0)
public abstract class AbstractDatabaseScriptIniter
    implements ApplicationListener<DataSourceReadyEvent> {
  private static final Logger logger = LoggerFactory.getLogger(StandardDatabaseScriptIniter.class);

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
      try {
        String evSql = getEvaluateSql(databaseType);
        if (Strings.isNullOrEmpty(evSql)) {
          return;
        }
        ScriptUtils.executeSqlScript(
            dataSource.getConnection(), new ByteArrayResource(evSql.getBytes(Charsets.UTF_8)));
      } catch (DataAccessException e) {
        Throwable throwable = Throwables.getRootCause(e);
        String msg = throwable.getMessage();
        if (throwable.getClass().getName().endsWith("MySQLSyntaxErrorException")
            && msg.endsWith("doesn't exist")) {
          List<String> files = getInitSqlFile(databaseType);
          if (Env.isOnline()) {
            logger.error("组件相关表不存在，请初始化[{}]", files);
            Apps.shutdown();
          }
          files.forEach(sqlPath -> exeSqlFile(dataSource, sqlPath));
        }
      }
    } catch (SQLException e) {
      throw new AppConfigException(e);
    }
  }

  /** 判断表是否存在 */
  public abstract String getEvaluateSql(DatabaseType databaseType);

  /** 数据库初始化脚本 */
  public abstract List<String> getInitSqlFile(DatabaseType databaseType);

  private void exeSqlFile(DataSource dataSource, String sqlpath) {
    logger.info("发现数据库基础数据还没有初始化，开始初始化:{}", sqlpath);
    try {
      Resource scriptResource = ApplicationContextHolder.get().getResource("classpath:" + sqlpath);
      EncodedResource encodedResource = new EncodedResource(scriptResource, Charsets.UTF_8);
      ScriptUtils.executeSqlScript(dataSource.getConnection(), encodedResource);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
