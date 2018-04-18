package com.acooly.module.defence;

import com.acooly.module.defence.csrf.CookieCsrfTokenRepository;
import com.acooly.module.defence.csrf.CsrfAccessDeniedHandlerImpl;
import com.acooly.module.defence.csrf.CsrfFilter;
import com.acooly.module.defence.csrf.RequireCsrfProtectionMatcher;
import com.acooly.module.defence.url.param.SecurityParamHandlerMethodArgumentResolver;
import com.acooly.module.defence.xss.XssDefenseFilter;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".csrf.enable", matchIfMissing = true)
    public static class CSRFAutoConfigration {
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
            registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp").toArray(new String[0]));
            registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
            registration.setName("csrfDefenseFilter");
            return registration;
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".xss.enable", matchIfMissing = true)
    public static class XssAutoConfigration {
        @Bean
        public FilterRegistrationBean xssFilter(DefenceProperties defenceProperties) {
            XssDefenseFilter filter = new XssDefenseFilter();
            filter.setDefenceProperties(defenceProperties);
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(filter);
            registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp").toArray(new String[0]));
            registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
            registration.setName("xssDefenseFilter");
            return registration;
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(value = DefenceProperties.PREFIX + ".url.enable", matchIfMissing = true)
    @ComponentScan("com.acooly.module.defence.url")
    public static class UrlAutoConfigration {
        @Bean
        public WebMvcConfigurerAdapter urlWebMvcConfigurerAdapter() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                    argumentResolvers.add(securityParamHandlerMethodArgumentResolver());
                }
            };
        }

        @Bean
        public SecurityParamHandlerMethodArgumentResolver securityParamHandlerMethodArgumentResolver() {
            return new SecurityParamHandlerMethodArgumentResolver();
        }
    }

}
