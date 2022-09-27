/**
 * acooly-component-web
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-24 21:24
 */
package com.acooly.module.web;

import com.acooly.module.web.filter.HttpsOnlyFilter;
import com.acooly.module.web.formatter.BigMoneyFormatter;
import com.acooly.module.web.formatter.DBMapFormatter;
import com.acooly.module.web.formatter.MoneyFormatter;
import com.acooly.module.web.freemarker.IncludePage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.utility.XmlEscape;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangpu
 */
@Slf4j
@Configuration
@EnableWebMvc
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class, WebProperties.class})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ComponentScan
@SuppressWarnings("unchecked")
public class WebAutoConfig implements WebMvcConfigurer {

    public static final String SIMPLE_URL_MAPPING_VIEW_CONTROLLER = "simpleUrlMappingViewController";

    @Autowired
    private WebProperties webProperties;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 扩展页面跳转
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 扩展设置welcome-file（acooly.web.welcome-file）
        if (!Strings.isNullOrEmpty(webProperties.getWelcomeFile())) {
            String welcomeFile = webProperties.getWelcomeFile();
            if (!welcomeFile.startsWith("/")) {
                welcomeFile = "forward:/" + welcomeFile;
            } else {
                welcomeFile = "forward:" + welcomeFile;
            }
            registry.addViewController("/").setViewName(welcomeFile);
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 默认为true，是否使用尾斜杠匹配：如果设置为true，则“/hello”和“/hello/”都能匹配
        configurer.setUseTrailingSlashMatch(true);
        configurer.setUseSuffixPatternMatch(true);
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 和底版本原逻辑保持一致：指定MVC的URL后置为html,已便于其他组件安全控制的粒度。不用去排除资源文件
        configurer.mediaType("html", MediaType.APPLICATION_JSON);
    }

    /**
     * 添加自定义类型格式化
     * 目前默认包括：
     * 1、Money的格式化和解析（元格式:####.##）
     * 2、DBMap的格式化和解析，主要用于EAV的数据存储JSON
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (webProperties.isEnableMoneyDisplayYuan()) {
            registry.addFormatter(new MoneyFormatter());
        }
        registry.addFormatter(new BigMoneyFormatter());
        registry.addFormatter(new DBMapFormatter());
    }


    /**
     * 扩展视图解析
     *
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // Freemarker扩展指令
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("requestContextAttribute", "rc");
        attributes.put("xml_escape", new XmlEscape());
        attributes.put("includePage", new IncludePage());
        String ssoEnable = System.getProperty("acooly.sso.freemarker.include");
        if (!StringUtils.isEmpty(ssoEnable)) {
            attributes.put("ssoEnable", Boolean.valueOf(ssoEnable));
        }
        registry.freeMarker().attributes(attributes);

        // JSP设置：如果开启则设置前缀和后缀
        if (webProperties.getJsp().isEnable()) {
            registry.jsp(webProperties.getJsp().getPrefix(), webProperties.getJsp().getSuffix());
        }
    }


    /**
     * 配置模板直接映射bean
     */
    @Bean
    public SimpleUrlHandlerMapping directUrlHandlerMapping(WebProperties webProperties) {
        SimpleUrlHandlerMapping directUrlHandlerMapping = new SimpleUrlHandlerMapping();
        directUrlHandlerMapping.setOrder(Integer.MAX_VALUE - 2);
        Map<String, Object> urlMap = new HashMap<>();
        for (String url : webProperties.buildMappingUrlList()) {
            urlMap.put(url, SIMPLE_URL_MAPPING_VIEW_CONTROLLER);
        }
        directUrlHandlerMapping.setUrlMap(urlMap);
        return directUrlHandlerMapping;
    }

    /**
     * 配置模板直接映射controller
     */
    @Bean(name = SIMPLE_URL_MAPPING_VIEW_CONTROLLER)
    public SimpleUrlMappingViewController simpleUrlMappingViewController(WebProperties webProperties) {
        SimpleUrlMappingViewController simpleUrlMappingViewController = new SimpleUrlMappingViewController();
        Map<String, String> viewNameMap = webProperties.buildViewNameMap();
        if (!viewNameMap.isEmpty()) {
            log.info("配置url直接映射模板:{}", viewNameMap);
        }
        simpleUrlMappingViewController.setViewNameMap(viewNameMap);
        return simpleUrlMappingViewController;
    }

    @Bean
    @ConditionalOnBean(HiddenHttpMethodFilter.class)
    public FilterRegistrationBean disableHiddenHttpMethodFilter(
            HiddenHttpMethodFilter filter, WebProperties webProperties) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(webProperties.isHiddenHttpMethodFilterEnable());
        return registration;
    }


    @Bean
    @ConditionalOnBean(FormContentFilter.class)
    public FilterRegistrationBean disableHttpPutFormContentFilter(
            FormContentFilter filter, WebProperties webProperties) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(webProperties.isHttpPutFormContentFilterEnable());
        return registration;
    }

    @Bean
    @ConditionalOnProperty("acooly.web.httpsOnly")
    public HttpsOnlyFilter httpsOnlyFilter() {
        return new HttpsOnlyFilter();
    }


    /**
     * 配置Freemarker的自定义的指令扩展
     *
     * @throws Exception
     */
    @PostConstruct
    public void setConfigure() throws Exception {
        // 扩展Freemarker指令
        freeMarkerConfigurer.getConfiguration().setSharedVariable("includePage", new IncludePage());
        String ssoEnable = System.getProperty("acooly.sso.freemarker.include");
        if (!StringUtils.isEmpty(ssoEnable)) {
            freeMarkerConfigurer.getConfiguration().setSharedVariable("ssoEnable", Boolean.valueOf(ssoEnable));
        }
        // 扩展自定义的tags
        List<String> tlds = Lists.newArrayList();
        tlds.add("/META-INF/form_tag.tld");
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(tlds);

    }

}
