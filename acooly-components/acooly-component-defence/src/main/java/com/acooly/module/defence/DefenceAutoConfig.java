package com.acooly.module.defence;

import com.acooly.module.defence.csrf.CookieCsrfTokenRepository;
import com.acooly.module.defence.csrf.CsrfFilter;
import com.acooly.module.defence.csrf.RequireCsrfProtectionMatcher;
import com.acooly.module.defence.exception.AccessDeniedHandler;
import com.acooly.module.defence.exception.handle.AccessDeniedHandlerImpl;
import com.acooly.module.defence.exception.handle.CsrfAccessDeniedHandlerImpl;
import com.acooly.module.defence.filter.HostHeaderAttackDefenceFilter;
import com.acooly.module.defence.url.param.SecurityParamHandlerMethodArgumentResolver;
import com.acooly.module.defence.xss.XssDefenseFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({DefenceProperties.class})
public class DefenceAutoConfig {

    @Autowired
    private DefenceProperties defenceProperties;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(defenceProperties.getHha().getErrorPage());
        return accessDeniedHandler;
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".hha.enable", matchIfMissing = false)
    public static class HhaAutoConfiguration {
        @Bean
        public FilterRegistrationBean hhaFilter(DefenceProperties defenceProperties,
                                                AccessDeniedHandler accessDeniedHandler) {
            HostHeaderAttackDefenceFilter hhaFilter = new HostHeaderAttackDefenceFilter();
            List<String> excludes = Lists.newArrayList();
            for (List<String> list : defenceProperties.getHha().getExclusions().values()) {
                excludes.addAll(list);
            }
            hhaFilter.setAccessDeniedHandler(accessDeniedHandler);
            hhaFilter.setDefenceProperties(defenceProperties);
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(hhaFilter);
            registration.addUrlPatterns("/*");
            registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
            registration.setName("hhaDefenseFilter");
            return registration;
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".csrf.enable", matchIfMissing = true)
    public static class CsrfAutoConfiguration {
        @Bean
        public FilterRegistrationBean csrfFilter(DefenceProperties defenceProperties) {
            CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();
            CsrfFilter csrfFilter = new CsrfFilter(tokenRepository);
            List<String> excludes = Lists.newArrayList();
            for (List<String> list : defenceProperties.getCsrf().getExclusions().values()) {
                excludes.addAll(list);
            }
            csrfFilter.setRequireCsrfProtectionMatcher(new RequireCsrfProtectionMatcher(excludes));
            CsrfAccessDeniedHandlerImpl csrfAccessDeniedHandler = new CsrfAccessDeniedHandlerImpl();
            csrfAccessDeniedHandler.setErrorPage(defenceProperties.getCsrf().getErrorPage());
            csrfFilter.setAccessDeniedHandler(csrfAccessDeniedHandler);
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(csrfFilter);
            registration.addUrlPatterns("/*");
            registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
            registration.setName("csrfDefenseFilter");
            return registration;
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".xss.enable", matchIfMissing = true)
    public static class XssAutoConfiguration {
        @Bean
        public FilterRegistrationBean xssFilter(DefenceProperties defenceProperties) {
            XssDefenseFilter filter = new XssDefenseFilter();
            filter.setDefenceProperties(defenceProperties);
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(filter);
            registration.addUrlPatterns("/*");
            registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
            registration.setName("xssDefenseFilter");
            return registration;
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".url.enable", matchIfMissing = true)
    @ComponentScan("com.acooly.module.defence.url")
    public static class UrlAutoConfigration implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new SecurityParamHandlerMethodArgumentResolver());
        }

    }

}
