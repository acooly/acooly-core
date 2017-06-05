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
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.vendor.OracleValidConnectionChecker;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/** @author qiubo */
@ConfigurationProperties(prefix = DruidProperties.PREFIX)
@Getter
@Setter
public class DruidProperties implements BeanClassLoaderAware {

  public static final String PREFIX = "acooly.ds";
  public static final String ENABLE_KEY = PREFIX + ".enable";

  public static final int DEFAULT_SLOW_SQL_THRESHOLD = 1000;

  private static final int ORACLE_MAX_ACTIVE = 300;
  private static final int MYSQL_MAX_ACTIVE = 300;

  private String prefix = PREFIX;
  /** 是否启用此组件 */
  private boolean enable = true;

  /** 必填：jdbc url */
  //@NotBlank(message = "数据库连接不能为空")
  //用jsr303信息展示太不直观
  private String url;

  /** 必填：数据库用户名 */
  private String username;

  /** 必填：数据库密码 */
  private String password;

  /** 初始连接数 */
  private Integer initialSize = 5;

  /** 最小空闲连接数 */
  private Integer minIdle = 20;

  /** 最大连接数,支持hera动态修改 */
  private Integer maxActive = 300;

  /** 获取连接等待超时的时间 */
  private Integer maxWait = 10000;

  /** 慢sql日志阈值，超过此值则打印日志 */
  private Integer slowSqlThreshold = DEFAULT_SLOW_SQL_THRESHOLD;

  /** 大结果集阈值，超过此值则打印日志 */
  private Integer maxResultThreshold = 1000;

  /** 是否在非线上环境开启打印sql，默认开启 */
  private boolean showSql = true;

  private boolean testOnBorrow = false;

  private ClassLoader beanClassLoader;

  private Checker checker = new Checker();

  public static String normalizeUrl(String url) {
    if (isMysql(url) && !url.contains("?")) {
      return url
          + "?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false";
    }
    return url;
  }

  public static boolean isMysql(String url) {
    return url.toLowerCase().startsWith("jdbc:mysql");
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

  public void check() {
    if (enable) {
      Assert.hasText(url, "数据库连接" + PREFIX + ".url不能为空");
      Assert.hasText(username, "数据库用户名" + PREFIX + ".username不能为空");
      Assert.hasText(password, "数据库密码" + PREFIX + ".password不能为空");
    }
  }

  public String getUrl() {
    return normalizeUrl(this.url);
  }

  public boolean mysql() {
    return isMysql(this.url);
  }

  /** 通过当前配置创建datasource */
  DruidDataSource build() {
    this.check();
    if (this.beanClassLoader == null) {
      this.beanClassLoader = ClassUtils.getDefaultClassLoader();
    }
    DruidDataSource dataSource = new DruidDataSource();
    // 基本配置
    dataSource.setDriverClassLoader(this.getBeanClassLoader());
    dataSource.setUrl(this.getUrl());
    dataSource.setUsername(this.getUsername());
    dataSource.setPassword(this.getPassword());
    //应用程序可以自定义的参数
    dataSource.setInitialSize(this.getInitialSize());
    dataSource.setMinIdle(this.getMinIdle());

    if (mysql()) {
      maxActive = Math.max(maxActive, MYSQL_MAX_ACTIVE);
      System.setProperty("spring.jpa.database", "MYSQL");
    } else {
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

    if (this.mysql()) {
      String validationQuery = "select 'x'";
      dataSource.setValidationQuery(validationQuery);
      dataSource.setValidationQueryTimeout(2);
    } else {
      System.setProperty("druid.oracle.pingTimeout", "5");
      dataSource.setValidConnectionChecker(new OracleValidConnectionChecker());
    }
    //fixme 开启ps cache
    //dataSource.setPoolPreparedStatements(!druidProperties.mysql());
    Properties properties = new Properties();
    if (this.isShowSql()) {
      properties.put("yiji.ds.logForHumanRead", Boolean.TRUE.toString());
    }
    if (!Env.isOnline()) {
      if (Apps.isDevMode()) {
        properties.put("yiji.ds.slowSqlMillis", Integer.toString(0));
      } else {
        //线下测试时，执行时间超过100ms就打印sql，用户可以设置为0，每条sql语句都打印
        properties.put(
            "yiji.ds.slowSqlMillis",
            Integer.toString(Math.min(this.getSlowSqlThreshold(), DEFAULT_SLOW_SQL_THRESHOLD)));
      }
    } else {
      //线上运行时，阈值选最大值。有可能线下测试设置为0，方便调试
      properties.put(
          "yiji.ds.slowSqlMillis",
          Integer.toString(Math.max(this.getSlowSqlThreshold(), DEFAULT_SLOW_SQL_THRESHOLD)));
    }
    properties.put("yiji.ds.maxResult", Integer.toString(this.getMaxResultThreshold()));
    dataSource.setConnectProperties(properties);
    try {
      dataSource.init();
    } catch (SQLException e) {
      throw new AppConfigException("druid连接池初始化失败", e);
    }

    return dataSource;
  }

  @Data
  public class Checker {
    /** 检查createTime和updateTime */
    private boolean checkColumn;
    /** 排除的表名在启动的时候不检查 创建时间、更新时间 这两个字段 */
    private Map<String, String> excludedColumnTables = Maps.newHashMap();
  }
}
