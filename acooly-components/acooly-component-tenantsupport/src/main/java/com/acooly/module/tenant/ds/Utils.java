package com.acooly.module.tenant.ds;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.dao.support.DataSourceReadyEvent;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.StringUtils;
import com.acooly.module.ds.DruidProperties;
import com.acooly.module.ds.DruidProperties.TenantDsProps;
import com.acooly.module.ds.check.DBPatchChecker;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Slf4j
public class Utils {


    public static DataSource createDs( DruidProperties druidProperties ) {
        DataSource dataSource;
        try {
            if (druidProperties == null) {
                druidProperties = new DruidProperties();
                EnvironmentHolder.buildProperties(druidProperties, DruidProperties.PREFIX);
            }
            dataSource = druidProperties.build();
            if (druidProperties.isAutoCreateTable()) {
                ApplicationContextHolder.get().publishEvent(new DataSourceReadyEvent(dataSource));
            }
            new DBPatchChecker(druidProperties).check(dataSource);
            return dataSource;
        } catch (Exception e) {
            //这种方式有点挫，先就这样吧
            log.error("初始化数据库连接池异常，关闭应用", e);
            System.exit(0);
            throw new AppConfigException("数据源配置异常", e);
        }
    }

    public static DruidProperties replaceDefaultProps( DruidProperties druidProperties,
            TenantDsProps tenantDsProps ) {

        if (tenantDsProps.getMinIdle() != null) {
            druidProperties.setMinIdle(tenantDsProps.getMinIdle());
        }
        if (tenantDsProps.getInitialSize() != null) {
            druidProperties.setInitialSize(tenantDsProps.getInitialSize());
        }
        if (tenantDsProps.getMaxActive() != null) {
            druidProperties.setMaxActive(tenantDsProps.getMaxActive());
        }
        if (tenantDsProps.getMaxWait() != null) {
            druidProperties.setMaxWait(tenantDsProps.getMaxWait());
        }
        if (!StringUtils.isEmpty(tenantDsProps.getUrl())) {
            druidProperties.setUrl(tenantDsProps.getUrl());
        }
        if (!StringUtils.isEmpty(tenantDsProps.getUsername())) {
            druidProperties.setUsername(tenantDsProps.getUsername());
        }
        if (!StringUtils.isEmpty(tenantDsProps.getPassword())) {
            druidProperties.setPassword(tenantDsProps.getPassword());
        }
        if (MapUtils.isNotEmpty(tenantDsProps.getDbPatchs())) {
            druidProperties.setDbPatchs(tenantDsProps.getDbPatchs());
        }
        return druidProperties;
    }
}
