/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-26 10:48 创建
 */
package com.acooly.module.security.config;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.module.ds.DataSourceReadyEvent;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class DatabaseScriptIniter implements ApplicationListener<DataSourceReadyEvent> {
	private boolean isMysql;
	private boolean needInitScript;
	
	@Override
	public void onApplicationEvent(DataSourceReadyEvent event) {
		DataSource dataSource = (DataSource) event.getSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		evaluateInit(jdbcTemplate);
		if (needInitScript) {
			log.info("发现数据库基础数据还没有初始化，开始初始化:");
			if (isMysql) {
				try {
					Resource scriptResource = ApplicationContextHolder.get()
						.getResource("classpath:META-INF/database/mysql/mysql.sql");
					ScriptUtils.executeSqlScript(dataSource.getConnection(), scriptResource);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
	}
	
	private void evaluateInit(JdbcTemplate jdbcTemplate) {
		try {
			jdbcTemplate.execute("SELECT count(*) FROM SYS_ROLE");
		} catch (DataAccessException e) {
			Throwable throwable = Throwables.getRootCause(e);
			String msg = throwable.getMessage();
			if (throwable.getClass().getName().endsWith("MySQLSyntaxErrorException") && msg.endsWith("doesn't exist")) {
				isMysql = true;
				needInitScript = true;
			}
		}
	}
}
