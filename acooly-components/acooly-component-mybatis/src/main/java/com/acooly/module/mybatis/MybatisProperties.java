/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * yanglie@yiji.com 2015-09-09 09:20 创建
 *
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.exception.AppConfigException;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.ibatis.session.LocalCacheScope;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

import static com.acooly.core.common.boot.listener.AcoolyApplicationRunListener.COMPONENTS_PACKAGE;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
@Data
public class MybatisProperties implements InitializingBean {
    public static final String PREFIX = "acooly.mybatis";

    private static final String MAPPER_RESOURCE_PATTERN =
            ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mybatis/**/*Mapper.xml";

    /**
     * 是否启用此组件
     */
    private boolean enable = true;

    /**
     * 可选: mybatis配置 ,ref:http://www.mybatis.org/mybatis-3/configuration.html#settings
     */
    private Map<String, String> settings;
    /**
     * 可选：自定义类型处理器TypeHandler所在的包，多个包用逗号分隔
     */
    private String typeHandlersPackage;

    private Map<String, String> typeAliasesPackage = Maps.newHashMap();

    private String configLocation;

    /**
     * 扩展dao扫描包
     */
    private Map<String, String> daoScanPackages = Maps.newHashMap();

    private boolean supportMultiDataSource;
    private Map<String, Multi> multi = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (settings == null) {
            settings = Maps.newHashMap();
        }
        if (!settings.containsKey("localCacheScope")) {
            settings.put("localCacheScope", LocalCacheScope.STATEMENT.name());
        }
        settings.put("mapUnderscoreToCamelCase", Boolean.TRUE.toString());
        if (typeHandlersPackage == null) {
            typeHandlersPackage = "com.acooly.module.mybatis.typehandler";
        } else {
            typeHandlersPackage += ",com.acooly.module.mybatis.typehandler";
        }
        typeAliasesPackage.put("app", Apps.getBasePackage());
        typeAliasesPackage.put("components", COMPONENTS_PACKAGE);
        daoScanPackages.put("components", COMPONENTS_PACKAGE + ".**.dao");
        daoScanPackages.put("portlets", "com.acooly.portlets.**.dao");
        daoScanPackages.put("app", Apps.getBasePackage());
        if (supportMultiDataSource) {
            Assert.notEmpty(multi, "启用多数据源时，必须配置数据源信息");
            int primaryCount = 0;
            for (Multi m : multi.values()) {
                if (m.isPrimary()) {
                    primaryCount++;
                }
            }
            if (primaryCount != 1) {
                throw new AppConfigException("有且只能有一个主数据源：" + this.getMulti());
            }
        }
    }

    public Resource[] resolveMapperLocations() {
        try {
            return new PathMatchingResourcePatternResolver().getResources(MAPPER_RESOURCE_PATTERN);
        } catch (IOException e) {
            return null;
        }
    }

    @Data
    public static class Multi {
        /**
         * 数据源配置前缀
         */
        private String dsPrefix;
        /**
         * 当前数据源Dao包路径
         */
        private String scanPackage;
        /**
         * 是否为主数据库
         */
        private boolean primary;
    }
}
