package com.acooly.module.point;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.module.point.PointProperties.PREFIX;

@Configuration
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan
@AutoConfigureAfter(SecurityAutoConfig.class)
public class PointAutoConfig {
	@Bean
	public AbstractDatabaseScriptIniter pointScriptIniter() {
		return new AbstractDatabaseScriptIniter() {
			@Override
			public String getEvaluateSql(DatabaseType databaseType) {
				return "SELECT count(*) FROM point_trade";
			}
			
			@Override
			public List<String> getInitSqlFile(DatabaseType databaseType) {
				return Lists.newArrayList("META-INF/database/mysql/point.sql","META-INF/database/mysql/point_urls.sql");
			}
		};
	}
}
