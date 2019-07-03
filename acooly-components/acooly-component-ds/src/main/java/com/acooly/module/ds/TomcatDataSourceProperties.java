package com.acooly.module.ds;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class TomcatDataSourceProperties {
    public DataSource build(DruidProperties druidProperties) {
        return null;
//        log.info("使用tomcat数据源");
//
//        org.apache.tomcat.jdbc.pool.DataSource dataSource =
//                new org.apache.tomcat.jdbc.pool.DataSource();
//        dataSource.setUrl(druidProperties.getUrl());
//        dataSource.setUsername(druidProperties.getUsername());
//        dataSource.setPassword(druidProperties.getPassword());
//        if (druidProperties.mysql()) {
//            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//            dataSource.setValidationQuery("select 'x'");
//
//        } else {
//            throw new UnsupportedOperationException("暂不支持其他数据库");
//        }
//        dataSource.setMinIdle(druidProperties.getMinIdle());
//        dataSource.setMaxActive(druidProperties.getMaxActive());
//        dataSource.setMaxWait(druidProperties.getMaxWait());
//        dataSource.setTestWhileIdle(true);
//        dataSource.setTestOnBorrow(true);
//        return dataSource;
    }
}
