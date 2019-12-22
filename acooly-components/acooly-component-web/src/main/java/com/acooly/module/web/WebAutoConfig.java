/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-06-16 17:52 创建
 *
 */
package com.acooly.module.web;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.module.web.filter.HttpsOnlyFilter;
import com.acooly.module.web.formatter.DBMapFormatter;
import com.acooly.module.web.formatter.MoneyFormatter;
import com.acooly.module.web.freemarker.IncludePage;
import com.acooly.module.web.freemarker.ShiroPrincipalTag;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.utility.XmlEscape;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo
 */
@Slf4j
@Configuration
@EnableWebMvc
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@EnableConfigurationProperties({
        WebMvcProperties.class,
        ResourceProperties.class,
        WebProperties.class
})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ComponentScan
public class WebAutoConfig implements WebMvcConfigurer, ApplicationContextAware, InitializingBean {

    public static final String SIMPLE_URL_MAPPING_VIEW_CONTROLLER = "simpleUrlMappingViewController";

    @Autowired
    private WebProperties webProperties;
    @Autowired
    private freemarker.template.Configuration configuration;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private ApplicationContext applicationContext;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 设置 welcome-file
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

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 和底版本原逻辑保持一致：指定MVC的URL后置为html,已便于其他组件安全控制的粒度。不用去排除资源文件
        configurer.mediaType("html", MediaType.APPLICATION_JSON);
    }

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.setUseRegisteredSuffixPatternMatch(true);
//        AntPathMatcher antPathMatcher = new AntPathMatcher("/**/*.html");
//        configurer.setPathMatcher(antPathMatcher)
//    }


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
        registry.addFormatter(new DBMapFormatter());
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("requestContextAttribute", "rc");
        attributes.put("xml_escape", new XmlEscape());

        attributes.put("includePage", new IncludePage());
        attributes.put("shiroPrincipal", new ShiroPrincipalTag());
        String ssoEnable = System.getProperty("acooly.sso.freemarker.include");
        if (!StringUtils.isEmpty(ssoEnable)) {
            attributes.put("ssoEnable", Boolean.valueOf(ssoEnable));
        }
        registry.freeMarker().attributes(attributes);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, WebMvcAutoConfiguration> beansOfType =
                applicationContext.getBeansOfType(WebMvcAutoConfiguration.class);
        if (beansOfType.isEmpty()) {
            log.error("spring mvc 没有正确加载WebMvcAutoConfiguration,原因可能有:");
            log.error("1. JavaConfig中配置了@EnableWebMvc");
            log.error("2. 引入了spring-mvc xml配置文件");
        }


        // 扩展freemarker
        if (EnvironmentHolder.get()
                .getProperty(WebProperties.Jsp.ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
            try {
                InternalResourceViewResolver internalResourceViewResolver =
                        applicationContext.getBean(InternalResourceViewResolver.class);
                internalResourceViewResolver.setPrefix(webProperties.getJsp().getPrefix());
                internalResourceViewResolver.setSuffix(webProperties.getJsp().getSuffix());


            } catch (BeansException e) {
                // do nothing
            }
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

        MultiTemplateLoader templateLoaderOld = (MultiTemplateLoader) freeMarkerConfigurer.getConfiguration().getTemplateLoader();

        List<TemplateLoader> templateLoaders = Lists.newLinkedList();
        for (int i = 0; i < templateLoaderOld.getTemplateLoaderCount(); i++) {
            templateLoaders.add(templateLoaderOld.getTemplateLoader(i));
        }
        templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, "/templates"));
        templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, "/templates/common"));

        freeMarkerConfigurer.getConfiguration().setTemplateLoader(new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[]{})));

        configuration.setSharedVariable("includePage", new IncludePage());
        configuration.setSharedVariable("shiroPrincipal", new ShiroPrincipalTag());
        configuration.setSharedVariable("shiro", new ShiroTags());
        String ssoEnable = System.getProperty("acooly.sso.freemarker.include");
        if (!StringUtils.isEmpty(ssoEnable)) {
            configuration.setSharedVariable("ssoEnable", Boolean.valueOf(ssoEnable));
        }
    }

}
