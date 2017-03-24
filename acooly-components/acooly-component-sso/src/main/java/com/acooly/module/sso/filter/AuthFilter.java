
package com.acooly.module.sso.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.sso.*;
import com.acooly.module.sso.dic.AuthConstants;
import com.acooly.module.sso.dic.AuthResult;
import com.acooly.module.sso.support.AuthFilterUtil;
import com.acooly.module.sso.support.DefaultRequestMatcher;
import com.acooly.module.sso.support.RequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author shuijing
 */
public class AuthFilter implements Filter {

    private static final LoginAuthProcessor defaultLoginAuthentication = new DefaultLoginAuth();

    private ApplicationContext applicationContext;

    private ServletContext servletContext;


    /**
     * 系统登录地址
     */
    private String loginUrl;

    private RequestMatcher requestMatcher;


    private Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        requestMatcher = new DefaultRequestMatcher(
            AuthFilterUtil.getConfigProperty(filterConfig, applicationContext,
                AuthConstants.KEY_STATIC_RESOURCE),
            AuthFilterUtil.getConfigProperty(filterConfig, applicationContext,
                AuthConstants.KEY_OPEN));
        // 重定向登录地址
        loginUrl = AuthFilterUtil.getAdLoginUrl();
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
        // 获取 requestUrl, queryString, 去掉 queryString 中的 jwt 和 secret 参数, 获取 compactJws (先从 request 中取,没有再从 cookie 中取)
        String requestURL = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();
        // 从 request 中获取 jwt
        String compactJws = AuthFilterUtil.getCompactJwt(httpServletRequest);

        AuthResult result = (AuthResult) defaultLoginAuthentication
            .loginAuthentication(compactJws, loginUrl, httpServletRequest, httpServletResponse);
        // 根据认证结果处理逻辑
        switch (result) {
            case LOGIN_URL_NULL:
                logger.error(AuthResult.LOGIN_URL_NULL.getDescription());
                throw new RuntimeException(AuthResult.LOGIN_URL_NULL.getDescription());
            case LOGIN_ERROR_DOMAIN:
                logger.error(AuthResult.LOGIN_ERROR_DOMAIN.getDescription());
                throw new RuntimeException(AuthResult.LOGIN_ERROR_DOMAIN.getDescription());
            case LOGIN_REDIRECT:
                AuthFilterUtil.doRedirectLogin(httpServletResponse, loginUrl, requestURL, queryString);
                break;
            case LOGIN_AUTHENTICATION_TIME_OUT:
                AuthFilterUtil.doRedirectLogin(httpServletResponse, loginUrl, requestURL, queryString);
                break;
            case AUTHENTICATION_TAMPER:
                logger.error(AuthResult.AUTHENTICATION_TAMPER.getDescription());
                throw new RuntimeException(AuthResult.AUTHENTICATION_TAMPER.getDescription());
            case AUTHENTICATION_ACCESS:
                chain.doFilter(request, response);
                break;
            default:
                chain.doFilter(request, response);
                break;
        }

    }

    @Override
    public void destroy() {

    }

}