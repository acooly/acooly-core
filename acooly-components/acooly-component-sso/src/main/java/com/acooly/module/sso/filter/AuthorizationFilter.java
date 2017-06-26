
package com.acooly.module.sso.filter;

import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.sso.SSOProperties;
import com.acooly.module.sso.support.DefaultRequestMatcher;
import com.acooly.module.sso.support.RequestMatcher;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/** @author shuijing */
public class AuthorizationFilter implements Filter {
  public static int TIME_OUT = 15 * 1000;
  private Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

  private ApplicationContext applicationContext;

  private Map<String, CacheVo> cache = Maps.newConcurrentMap();

  private ServletContext servletContext;

  private SSOProperties ssoProperties;
  private RequestMatcher requestMatcher;
  private String rootPath = null;

  public AuthorizationFilter(SSOProperties ssoProperties) {
    this.ssoProperties = ssoProperties;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    servletContext = filterConfig.getServletContext();
    applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    requestMatcher = new DefaultRequestMatcher(ssoProperties.getSsoExcludeUrl());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;

    if (!requestMatcher.matches(httpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

    if (checkReferer(httpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }
    if (isPermittedFromCache(httpServletRequest)) {
      chain.doFilter(request, response);
    } else {
      response.setContentType("text/html;charset=UTF-8");
      response.getOutputStream().write("没有权限!请联系管理员!".getBytes(Charset.forName("UTF-8")));
    }
  }

  @Override
  public void destroy() {}

  private boolean isPermitted(HttpServletRequest request) throws MalformedURLException {
    long st = System.currentTimeMillis();
    if (rootPath == null) {
      String ssoServerUrl = ssoProperties.getSsoServerUrl();
      URL url = new URL(ssoServerUrl);
      String http = ssoServerUrl.substring(0, ssoServerUrl.indexOf("//") + 2);
      rootPath = http + url.getHost() + (url.getPort() == -1 ? "" : ":" + url.getPort());
    }
    String requestURI = request.getRequestURI();
    String permittedPath = rootPath + "/role/facade/isPermitted";
    String username = (String) request.getAttribute(JWTUtils.KEY_SUB_NAME);
    String body;
    try {
      body =
          HttpRequest.post(permittedPath.trim())
              .form("uri", requestURI)
              .form("username", username)
              .readTimeout(TIME_OUT)
              .connectTimeout(TIME_OUT)
              .trustAllCerts()
              .trustAllHosts()
              .body();
    } catch (Exception e) {
      logger.error("权限校验出错 uri is {}", requestURI, e);
      return false;
    }
    long et = System.currentTimeMillis();
    if (!StringUtils.isEmpty(body) && (body.contains("true") || body.contains("false"))) {
      Boolean permitted = Boolean.valueOf(body);
      logger.info(
          "sso 用户:{}权限校验结果:{},耗时:{} ms, url:{} ", username, permitted, (et - st), requestURI);
      return Boolean.valueOf(body);
    }
    logger.info("sso 用户:{}权限校验结果:{},耗时:{} ms, url:{} ", username, false, (et - st), requestURI);
    return false;
  }

  private boolean checkReferer(HttpServletRequest httpServletRequest) throws MalformedURLException {
    String referrer = httpServletRequest.getHeader("referer");
    if (!StringUtils.isEmpty(referrer)) {
      URL url = new URL(referrer);
      String host = url.getHost();
      String ssoServerUrl = ssoProperties.getSsoServerUrl();
      if (!StringUtils.isEmpty(ssoServerUrl) && ssoServerUrl.contains(host)) {
        return true;
      }
    }
    return false;
  }

  public boolean isPermittedFromCache(HttpServletRequest request) throws MalformedURLException {
    String url = request.getRequestURI();
    if (cache.containsKey(url)) {
      CacheVo cacheVo = cache.get(url);
      long startTime = cacheVo.getStartTime();
      long currentTime = System.currentTimeMillis();
      if ((currentTime - startTime) > ssoProperties.getAuthorizationCacheTime() * 60 * 1000) {
        //缓存失效，重新检验
        return reputCache(request);
      } else {
        return cacheVo.isPermitted;
      }
    } else {
      return reputCache(request);
    }
  }

  private boolean reputCache(HttpServletRequest request) throws MalformedURLException {
    boolean permitted = isPermitted(request);
    CacheVo cacheVo = new CacheVo();
    cacheVo.setIsPermitted(permitted);
    cacheVo.setStartTime(System.currentTimeMillis());
    cache.put(request.getRequestURI(), cacheVo);
    return permitted;
  }

  @Data
  private static class CacheVo {
    private Boolean isPermitted;
    private long startTime;
  }
}
