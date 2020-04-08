/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 14:32 创建
 *
 */
package com.acooly.module.tenant.ds;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.dao.support.DataSourceReadyEvent;
import com.acooly.core.utils.StringUtils;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.module.ds.DruidProperties;
import com.acooly.module.ds.DruidProperties.TenantDsProps;
import com.acooly.module.ds.PagedJdbcTemplate;
import com.acooly.module.ds.check.DBPatchChecker;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.druid.pool.DruidDataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author zhouxi
 */
@Configuration
@EnableConfigurationProperties({DruidProperties.class})
@ConditionalOnProperty(value = DruidProperties.ENABLE_KEY, matchIfMissing = true)
@Slf4j
public class JDBCAutoConfigX {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        if (MapUtils.isEmpty(druidProperties.getTenant())) {
            throw new RuntimeException("检测到租户数据源没有配置，请配置租户数据源");
        }
        DataSource dataSource;
        TenantDatasource tenantDatasource = new TenantDatasource();
        Map<String, String> tenantInfoMap = new HashMap<>();
        for (Entry<String, TenantDsProps> entry : druidProperties.getTenant().entrySet()) {
            DruidProperties copy = new DruidProperties();
            if (StringUtils.isEmpty(entry.getValue().getAlias())) {
                throw new RuntimeException("租户数据源:" + entry.getKey() + "没有指定别名");
            }
            tenantInfoMap.put(entry.getKey(), entry.getValue().getAlias());
            BeanCopier.copy(druidProperties, copy);
            log.info("init tenant Datasource: {} ", entry.getKey());
            DataSource source = Utils
                    .createDs(Utils.replaceDefaultProps(copy, entry.getValue()));
            if (druidProperties.isAutoCreateTable()) {
                ApplicationContextHolder.get().publishEvent(new DataSourceReadyEvent(source));
            }
            new DBPatchChecker(druidProperties).check(source);
            tenantDatasource.registerTenantDataSource(entry.getKey(),
                    (DruidDataSource) source);
        }
        try {
            Field field = TenantContext.class.getDeclaredField("allTenant");
            field.setAccessible(true);
            field.set(null, tenantInfoMap);
        } catch (Exception e) {
            //nothing
            log.error("error", e);
        }
        dataSource = tenantDatasource;
        return dataSource;
    }


    @Bean
    @ConditionalOnMissingBean(JdbcOperations.class)
    public JdbcTemplate jdbcTemplate( DataSource dataSource ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PagedJdbcTemplate pagedJdbcTemplate( DataSource dataSource ) {
        return new PagedJdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(NamedParameterJdbcOperations.class)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate( DataSource dataSource ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionTemplate.class)
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager platformTransactionManager ) {
        return new TransactionTemplate(platformTransactionManager);
    }

}