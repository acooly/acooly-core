/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-25 14:00 创建
 */
package com.acooly.module.jpa;

import com.acooly.core.common.boot.Apps;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

import static com.acooly.core.common.boot.listener.AcoolyApplicationRunListener.COMPONENTS_PACKAGE;
import static com.acooly.module.jpa.JPAProperties.PREFIX;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class JPAProperties implements InitializingBean {

    public static final String PREFIX = "acooly.jpa";
    public static final String ENABLE_KEY = PREFIX + ".enable";
    private boolean enable = true;
    private boolean diableScanCurrentProjectEntity = false;
    /**
     * 是否启用openEntityManagerInViewFilter
     */
    private boolean openEntityManagerInViewFilterEnable = true;
    /**
     * 默认对/manage/*路径启用openEntityManagerInViewFilter，如下添加其他路径，使用此配置，例如：
     *
     * acooly.jpa.openEntityManagerInViewFilterUrlPatterns[0]=*.xx
     */
    private List<String> openEntityManagerInViewFilterUrlPatterns = Lists.newArrayList();
    private Map<String, String> entityPackagesToScan = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() {
        if (diableScanCurrentProjectEntity) {
            entityPackagesToScan.put("app0", Apps.getBasePackage() + ".**.domain");
            entityPackagesToScan.put("app1", Apps.getBasePackage() + ".**.entity");
        }

        entityPackagesToScan.put("components0", COMPONENTS_PACKAGE + ".**.domain");
        entityPackagesToScan.put("components1", COMPONENTS_PACKAGE + ".**.entity");
        openEntityManagerInViewFilterUrlPatterns.add("/manage/*");
    }
}
