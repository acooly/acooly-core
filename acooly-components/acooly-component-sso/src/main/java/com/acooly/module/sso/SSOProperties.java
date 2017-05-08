package com.acooly.module.sso;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shuijing
 */
@Data
@ConfigurationProperties(SSOProperties.PREFIX)
public class SSOProperties {

    public static final String PREFIX = "acooly.sso";

    private boolean enable = true;

    @NotEmpty
    private String ssoServerUrl;

    /**
     * 不需要拦截处理的静态资源(ant规则，以逗号分隔)
     */
    private String ssoExcludeUrl;

    /**
     * 没有权限时跳转的链接
     */
    private String unauthorizedUrl = "/unauthorized.html";

}
