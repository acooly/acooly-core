package com.acooly.module.tenant.ds;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.StringUtils;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.druid.pool.DruidDataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Slf4j
public class TenantDatasource implements DataSource, Closeable {

    /**
     * put dataSourceHere
     */
    private Map<String, DruidDataSource> tenantDataSourceMap = new HashMap<>();


    public void registerTenantDataSource( String tenantId, DruidDataSource dataSource ) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new RuntimeException("租户Id不能为空白字符串");
        }
        if (tenantDataSourceMap.containsValue(tenantId)) {
            throw new RuntimeException("tenantId:" + tenantId + " 已经被注册过了，不能重复注册");
        }
        tenantDataSourceMap.put(tenantId, dataSource);
    }

    private DataSource acquireTenantDataSource() {
        String tenant = TenantContext.get();
        if (StringUtils.isEmpty(tenant)) {
            throw new BusinessException("线程上下文中没有租户信息,或者租户ID为空");
        }
        DataSource ds = tenantDataSourceMap.get(tenant);
        if (ds == null) {
            throw new BusinessException("找不到租户Id为:" + tenant + " 所对应的数据源");
        }
        log.debug("获取租户:{} 的数据源:{} 成功", tenant, ds);
        return ds;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return acquireTenantDataSource().getConnection();
    }

    @Override
    public Connection getConnection( String username, String password ) throws SQLException {
        return acquireTenantDataSource().getConnection(username, password);
    }

    @Override
    public <T> T unwrap( Class<T> iface ) throws SQLException {
        return acquireTenantDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor( Class<?> iface ) throws SQLException {
        return acquireTenantDataSource().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return acquireTenantDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter( PrintWriter out ) throws SQLException {
        acquireTenantDataSource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout( int seconds ) throws SQLException {
        acquireTenantDataSource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return acquireTenantDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return acquireTenantDataSource().getParentLogger();
    }

    @Override
    public void close() {
        tenantDataSourceMap.forEach(( k, v ) -> {
            log.info("close tenant {} datasource", k);
            v.close();
        });
    }
}
