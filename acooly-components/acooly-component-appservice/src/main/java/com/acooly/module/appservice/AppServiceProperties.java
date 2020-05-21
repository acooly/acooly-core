/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-09-22 15:43 创建
 */
package com.acooly.module.appservice;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.utils.validate.Validators;
import com.acooly.module.appservice.ex.ExceptionHandler;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(AppServiceProperties.PREFIX)
@Data
public class AppServiceProperties {

    public static final String PREFIX = "acooly.appservice";
    private static final String INTERNAL_EXCEPTION_HANDLER_PACKAGE =
            "com.acooly.module.appservice.ex," + Apps.getBasePackage();
    private boolean enable;
    /**
     * {@link AppService} annotation扫描路径
     */
    @NotBlank
    private String appServiceScanPackage = Apps.getBasePackage();

    /**
     * 扩展annotation扫描路径
     * 防止与`appServiceScanPackage`参数的冲突，增加该参与，用于在其他组件内非Apps.basePackage下定义扫描路径
     */
    private Map<String, String> scanPackages = Maps.newHashMap();

    /**
     * {@link ExceptionHandler} 实现类扫描路径
     */
    private String exceptionHanderScanPackage;

    /**
     * 默认参数校验实现类,可替换为其他实现类
     */
    private String parameterCheckFilterImpl =
            "com.acooly.module.appservice.filter.ParameterCheckFilter";

    public String getExceptionHanderScanPackage() {
        if (!StringUtils.isBlank(exceptionHanderScanPackage)) {
            return INTERNAL_EXCEPTION_HANDLER_PACKAGE + "," + exceptionHanderScanPackage.trim();
        }
        return INTERNAL_EXCEPTION_HANDLER_PACKAGE;
    }

    public void check() {
        Validators.assertJSR303(this);
    }
}
