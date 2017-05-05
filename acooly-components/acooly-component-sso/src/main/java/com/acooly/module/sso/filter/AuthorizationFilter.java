
package com.acooly.module.sso.filter;

import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.sso.DefaultLoginAuth;
import com.acooly.module.sso.LoginAuthProcessor;
import com.acooly.module.sso.SSOProperties;
import com.acooly.module.sso.support.DefaultRequestMatcher;
import com.acooly.module.sso.support.RequestMatcher;
import com.github.kevinsawicki.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author shuijing
 */
public class AuthorizationFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    public static int TIME_OUT = 5 * 1000;

    private static final LoginAuthProcessor defaultLoginAuthentication = new DefaultLoginAuth();

    private ApplicationContext applicationContext;

    private ServletContext servletContext;

    private SSOProperties ssoProperties;

    public AuthorizationFilter(SSOProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }

    private RequestMatcher requestMatcher;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        requestMatcher = new DefaultRequestMatcher(ssoProperties.getSsoExcludeUrl());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!requestMatcher.matches(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        if (checkReferer(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        if (isPermitted(httpServletRequest)) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect(ssoProperties.getUnauthorizedUrl());
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isPermitted(HttpServletRequest request) throws MalformedURLException {
        String ssoServerUrl = ssoProperties.getSsoServerUrl();
        String requestURI = request.getRequestURI();
        URL url = new URL(ssoServerUrl);
        String http = ssoServerUrl.substring(0, ssoServerUrl.indexOf("//") + 2);
        String rootPath = http + url.getHost() + (url.getPort() == -1 ? "" : ":" + url.getPort());
        String permittedPath = rootPath + "/role/facade/isPermitted";
        String username = (String) request.getAttribute(JWTUtils.KEY_SUB_NAME);
        String body = HttpRequest.post(permittedPath.trim()).form("uri", requestURI).form("username", username).readTimeout(TIME_OUT).connectTimeout(TIME_OUT).trustAllCerts().trustAllHosts().body();
        return Boolean.valueOf(body);
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

}