
package com.acooly.module.sso;

import com.acooly.module.sso.filter.AuthenticationFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static com.acooly.module.sso.SSOProperties.PREFIX;

/**
 * @author shuijing
 */
@Configuration
@EnableConfigurationProperties({SSOProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
public class SSOAutoConfig {

    @Autowired
    private SSOProperties ssoProperties;

    @Bean
    public FilterRegistrationBean ssoFilter() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setSSOExcludeUrl(ssoProperties.getSsoExcludeUrl());
        authenticationFilter.setLoginUrl(ssoProperties.getSsoServerUrl());
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp", "*.json").toArray(new String[0]));
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        registration.setName("ssoFilter");
        return registration;
    }
}
