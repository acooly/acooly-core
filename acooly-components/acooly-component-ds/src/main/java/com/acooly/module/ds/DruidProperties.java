/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-24 17:36 创建
 *
 */
package com.acooly.module.ds;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.Env;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.facade.InfoBase;
import com.acooly.core.utils.ToString;
import com.acooly.module.ds.check.DBPatch;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.vendor.OracleValidConnectionChecker;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = DruidProperties.PREFIX)
@Setter
@Getter
@Slf4j
public class DruidProperties extends InfoBase implements BeanClassLoaderAware {

    public static final String PREFIX = "acooly.ds";
    public static final String ENABLE_KEY = PREFIX + ".enable";

    public static final int DEFAULT_SLOW_SQL_THRESHOLD = 1000;

    private static final int ORACLE_MAX_ACTIVE = 300;
    private static final int MYSQL_MAX_ACTIVE = 300;

    private String prefix = PREFIX;
    /**
     * 是否启用此组件
     */
    private boolean enable = true;


    /**
     * 租户配置
     */
    private Map<String, TenantDsProps> tenant;


    /**
     * 必填：jdbc url
     */
    //@NotBlank(message = "数据库连接不能为空")
    //用jsr303信息展示太不直观
    private String url;

    /**
     * 必填：数据库用户名
     */
    private String username;

    /**
     * 必填：数据库密码
     */
    @ToString.Maskable(maskAll = true)
    private String password;

    /**
     * 初始连接数
     */
    private Integer initialSize = 5;

    /**
     * 最小空闲连接数
     */
    private Integer minIdle = 20;

    /**
     * 最大连接数,支持hera动态修改
     */
    private Integer maxActive = 300;

    /**
     * 获取连接等待超时的时间
     */
    private Integer maxWait = 10000;

    /**
     * 慢sql日志阈值，超过此值则打印日志
     */
    private Integer slowSqlThreshold = DEFAULT_SLOW_SQL_THRESHOLD;

    private boolean testOnBorrow = false;

//    @Deprecated
//    private boolean useTomcatDataSource = false;

    /**
     * 自动创建表
     */
    private boolean autoCreateTable = true;

    /**
     * 检查表是否缺少某些字段，如果缺少，启动报错。
     */
    @ToString.Invisible
    private Map<String, List<DBPatch>> dbPatchs;
    @ToString.Invisible
    private ClassLoader beanClassLoader;
    @ToString.Invisible
    private Checker checker = new Checker();

    public static String normalizeUrl(String url) {
        if (isMysql(url) && !url.contains("?")) {
            return url + "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false";
        }
        return url;
    }

    public static boolean isMysql(String url) {
        return url.toLowerCase().startsWith("jdbc:mysql");
    }

    public static boolean isOracle(String url) {
        return url.toLowerCase().startsWith("jdbc:oracle");
    }

    /**
     * 从环境配置中构建数据源
     *
     * @param prefix 配置前缀
     */
    public static DruidDataSource buildFromEnv(String prefix) {
        DruidProperties druidProperties = new DruidProperties();
        EnvironmentHolder.buildProperties(druidProperties, prefix);
        return druidProperties.build();
    }

    @Override
    public void check() {
        if (enable) {
            Assert.hasText(url, "数据库连接" + PREFIX + ".url不能为空");
            Assert.hasText(username, "数据库用户名" + PREFIX + ".username不能为空");
            Assert.hasText(password, "数据库密码" + PREFIX + ".password不能为空");
        }
    }

    //bug fix spring 初始化properties会做类型转换 会先调用get取旧的value，如果转换失败会返回旧的value，此时url有可能还没被set
    // 因此不要在get，set方法中写逻辑
//    public String getUrls() {
//        return normalizeUrl(this.url);
//    }


    public String fetchUrl() {
        return normalizeUrl(this.url);
    }

    public boolean mysql() {
        return isMysql(this.url);
    }

    public boolean oracle() {
        return isOracle(this.url);
    }

    /**
     * 通过当前配置创建datasource
     */
    public DruidDataSource build() {
        this.check();
        if (this.beanClassLoader == null) {
            this.beanClassLoader = ClassUtils.getDefaultClassLoader();
        }
        DruidDataSource dataSource = new DruidDataSource();
        // 基本配置
        dataSource.setDriverClassLoader(this.getBeanClassLoader());
        dataSource.setUrl(this.fetchUrl());
        dataSource.setUsername(this.getUsername());
        dataSource.setPassword(this.getPassword());
        //应用程序可以自定义的参数
        dataSource.setInitialSize(this.getInitialSize());
        dataSource.setMinIdle(this.getMinIdle());

        if (mysql()) {
            maxActive = Math.max(maxActive, MYSQL_MAX_ACTIVE);
            System.setProperty("spring.jpa.database", "MYSQL");
            // 设置支持utf8mb4
            dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
        } else if (oracle()) {
            maxActive = Math.max(maxActive, ORACLE_MAX_ACTIVE);
            System.setProperty("spring.jpa.database", "ORACLE");
        }
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(this.getMaxWait());
        //检测需要关闭的空闲连接间隔，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(300000);
        //连接在池中最小生存的时间
        dataSource.setMinEvictableIdleTimeMillis(3600000);
        dataSource.setTestWhileIdle(true);
        //从连接池中获取连接时不测试
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(false);
        dataSource.setValidationQueryTimeout(5);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(3600);
        dataSource.setLogAbandoned(true);
        if (this.mysql()) {
            String validationQuery = "select 'x'";
            dataSource.setValidationQuery(validationQuery);
            dataSource.setValidationQueryTimeout(2);
        } else if (oracle()) {
            System.setProperty("druid.oracle.pingTimeout", "5");
            dataSource.setValidConnectionChecker(new OracleValidConnectionChecker());
        }
        //fixme 开启ps cache
        //dataSource.setPoolPreparedStatements(!druidProperties.mysql());
        Properties properties = new Properties();
        properties.put("druid.stat.logSlowSql", Boolean.TRUE.toString());
        if (!Env.isOnline()) {
            properties.put("druid.stat.slowSqlMillis", Integer.toString(slowSqlThreshold));
        } else {
            //线上运行时，阈值选最大值。有可能线下测试设置为0，方便调试
            properties.put("druid.stat.slowSqlMillis", Integer.toString(Math.max(this.getSlowSqlThreshold(), DEFAULT_SLOW_SQL_THRESHOLD)));
        }
        dataSource.setConnectProperties(properties);

        try {
            dataSource.setFilters("stat");
            dataSource.init();
        } catch (SQLException e) {
            if (Apps.isDevMode() && mysql()) {
                if (e instanceof SQLSyntaxErrorException) {
                    if (e.getMessage().contains("Unknown database")) {
                        try {
                            DruidDataSource xdataSource = new DruidDataSource();
                            String url = DruidProperties.normalizeUrl(this.url.substring(0, this.url.lastIndexOf("/")) + "/mysql");
                            xdataSource.setUrl(url);
                            String dataBase = this.url.substring(this.url.lastIndexOf("/") + 1);
                            log.warn("配置文件中指定的数据库[{}]不存在，尝试创建数据库", dataBase);
                            String createDataBaseSql = "CREATE SCHEMA `" + dataBase + "` DEFAULT CHARACTER SET utf8mb4 collate utf8mb4_unicode_ci;";
                            xdataSource.setUsername(this.getUsername());
                            xdataSource.setPassword(this.getPassword());
                            xdataSource.init();
                            try (Connection connection = xdataSource.getConnection()) {
                                try {
                                    ScriptUtils.executeSqlScript(
                                            connection, new ByteArrayResource(createDataBaseSql.getBytes(Charsets.UTF_8)));
                                    log.warn("创建数据库[{}]成功", dataBase);
                                } catch (ScriptException e1) {
                                    throw new AppConfigException("druid连接池自动创建数据库失败", e);
                                }
                            }
                            xdataSource.close();
                        } catch (SQLException e1) {
                            throw new AppConfigException("druid连接池初始化失败", e);
                        }
                        return dataSource;
                    }
                }
            }
            throw new AppConfigException("druid连接池初始化失败", e);
        }
        return dataSource;
    }


    @Data
    public static class TenantDsProps {

        private String url;

        String username;

        @ToString.Maskable(maskAll = true)
        private String password;

        private Integer initialSize;


        private Integer minIdle;

        private Integer maxActive;

        private Integer maxWait;

        private String alias;

        /**
         * 检查表是否缺少某些字段，如果缺少，启动报错。
         */
        @ToString.Invisible
        private Map<String, List<DBPatch>> dbPatchs;
    }

    @Data
    public class Checker {
        /**
         * 检查createTime和updateTime
         */
        private boolean checkColumn;
        /**
         * 排除的表名在启动的时候不检查 创建时间、更新时间 这两个字段
         */
        private Map<String, String> excludedColumnTables = Maps.newHashMap();
    }
}
