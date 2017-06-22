/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-10-27 23:31 创建
 */
package com.acooly.module.security.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** @author qiubo */
@ConfigurationProperties(SecurityProperties.PREFIX)
@Data
public class SecurityProperties {

  public static final String PREFIX = "acooly.security";
  /** 是否启用shiro */
  private boolean enable = true;

  private Shiro shiro = new Shiro();
  private CSRF csrf = new CSRF();
  private Xss xss = new Xss();
  private Captcha captcha = new Captcha();

  /** 开启后shiro filter链都会设为不拦截，可在系统不需要任何授权、认证时开启 */
  private boolean shiroFilterAnon = false;

  @PostConstruct
  public void initXss() {
    this.xss.init();
  }

  @Getter
  @Setter
  public static class Shiro {

    /** 是否启用shiro */
    private boolean enable = true;

    /** 登录页面链接 */
    private String loginUrl = "/manage/login.html";

    /** 没有权限时跳转的链接 */
    private String unauthorizedUrl = "/unauthorized.html";

    /** 登录成功后的链接 */
    private String successUrl = "/manage/onLoginSuccess.html";
    /** 登录失败后的链接 */
    private String failedUrl = "/manage/onLoginFailure.html";
    /**
     * 对应shiro.ini中的[urls]标签，注意顺序，格式如：
     *
     * <p>
     *
     * <pre>
     * acooly.shiro.urls[0]./shiro/** = authc
     * acooly.shiro.urls[1]./** = anon
     * </pre>
     */
    private List<Map<String, String>> urls = Lists.newLinkedList();

    /**
     * 自定义Filter列表，对应shiro.ini中的[filters]标签，格式如：
     *
     * <p>
     *
     * <pre>
     *  yiji.shiro.filters.authc=com.yiji.neverstopfront.web.shiro.CaptchaFormAuthenticationFilter
     *  yiji.shiro.filters.admin=com.yiji.neverstopfront.web.shiro.AdminAuthorizationFilter
     *  houseProperty=com.yiji.neverstopfront.web.shiro.ServiceTypeAuthorizationFilter
     *  houseProperty.serviceType=HOUSE_PROPERTY
     *  installment=com.yiji.neverstopfront.web.shiro.ServiceTypeAuthorizationFilter
     *  installment.serviceType=INSTALLMENT
     *  </pre>
     *
     * <p>
     * </ul>
     */
    private LinkedHashMap<String, String> filters = Maps.newLinkedHashMap();

    public Shiro() {
      //添加默认url过滤器
      addUrlFilter("/manage/index.html", "authc");
      addUrlFilter("/manage/login.html", "authc");
      addUrlFilter("/manage/logout.html", "logout");
      addUrlFilter("/manage/error/**", "anon");
      addUrlFilter("/manage/assert/**", "anon");
      addUrlFilter("/manage/*.html", "anon");
      addUrlFilter("/manage/*.jsp", "user");
      addUrlFilter("/manage/layout/*", "user");
      addUrlFilter("/manage/system/*", "user");
      addUrlFilter("/manage/druid/**", "user");
      addUrlFilter("/manage/**", "urlAuthr");
      addUrlFilter("/**", "anon");
    }

    public void addUrlFilter(String key, String value) {
      Map<String, String> url = Maps.newHashMap();
      url.put(key, value);
      urls.add(url);
    }
  }

  @Getter
  @Setter
  public static class CSRF {
    private boolean enable = true;
    private Map<String, List<String>> exclusions = Maps.newHashMap();
    private String errorPage = "/error/csrf.htm";

    public CSRF() {
      List<String> list = Lists.newArrayList();
      list.add("/gateway.html");
      list.add("/ofile/upload.html");
      list.add("/mock/gateway/**");
      exclusions.put("security", list);
    }
  }

  @ToString
  public static class Xss {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Getter @Setter private boolean enable = true;
    /** 路径支持ant表达式 */
    @Getter @Setter private Map<String, List<String>> exclusions = Maps.newHashMap();

    private List<String> ex = Lists.newArrayList();

    public boolean matches(HttpServletRequest request) {
      if (ex != null) {
        String uri = request.getRequestURI();
        int idx = uri.indexOf(';');
        if (idx > -1) {
          uri = uri.substring(0, idx);
        }
        for (String ignoreAntPathMatcherPattern : ex) {
          if (antPathMatcher.match(ignoreAntPathMatcherPattern, uri)) {
            return false;
          }
        }
      }
      return true;
    }

    public void init() {
      exclusions.values().forEach(v -> ex.addAll(v));
    }
  }

  @Data
  public static class Captcha {
    private boolean enable = true;
    private String url = "/jcaptcha.jpg";
    private Kaptcha kaptcha = new Kaptcha();

    @Data
    public static class Kaptcha {
      /** 图片高 */
      private int height = 30;
      /** 图片宽 */
      private int width = 80;
      /** 验证码文字颜色 */
      private String fontColor = "34,114,200";
      /** 验证码文字大小 */
      private int fontSize = 24;
      /** 验证码长度 */
      private int charCount = 4;
    }
  }
}
