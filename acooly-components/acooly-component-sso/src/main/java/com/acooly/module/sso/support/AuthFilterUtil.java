
package com.acooly.module.sso.support;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.utils.security.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;


/**
 * @author shuijing
 */
public class AuthFilterUtil {

    /**
     * 重定向登录
     *
     * @param response
     * @param loginUrl
     * @param requestURL
     * @param queryString
     * @throws java.io.IOException
     */
    public static void doRedirectLogin(HttpServletResponse response, String loginUrl,
                                       String requestURL, String queryString) throws IOException {
        if (StringUtils.isNotBlank(queryString)) {
            requestURL += "?" + queryString;
        }
        //String targetUrl = Base64.encodeBase64String(requestURL.getBytes("utf-8"));
        response.setHeader("targetUrl", requestURL);
        response.sendRedirect(loginUrl + "?targetUrl=" + requestURL);
    }

    /**
     * 先读取 filter initParam, 没有值则读取 applicationContext 环境属性
     *
     * @param filterConfig
     * @param applicationContext
     * @param propertyKey
     * @return
     */
    public static String getConfigProperty(FilterConfig filterConfig, ApplicationContext applicationContext, String propertyKey) {
        String propertyValue = filterConfig.getInitParameter(propertyKey);
        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = applicationContext.getEnvironment().getProperty(propertyKey);
        }
        return propertyValue;
    }

    /**
     * 获取 jwt 信息
     *
     * @param httpServletRequest
     * @return
     */
    public static String getCompactJwt(HttpServletRequest httpServletRequest) {
        /**
         * 获取jwt顺序
         * 1、header
         * 2、request
         * 3、cookie
         */
        String compactJws = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(compactJws)) {
            compactJws = httpServletRequest.getParameter(JWTUtils.TYPE_JWT);
            if (StringUtils.isEmpty(compactJws)) {
                Cookie[] cookies = httpServletRequest.getCookies();
                compactJws = JWTUtils.getJwtFromCookie(cookies);
            }
        }
        return compactJws;
    }

}
