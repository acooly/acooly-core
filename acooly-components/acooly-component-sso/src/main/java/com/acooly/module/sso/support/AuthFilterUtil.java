
package com.acooly.module.sso.support;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.sso.dic.AuthConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;


/**
 * @author shuijing
 */
public class AuthFilterUtil {


    /**
     * 从 cookie 中获取 jwt
     *
     * @param cookies
     * @return
     */
    public static String getJwtFromCookie(Cookie[] cookies) {
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AuthConstants.TYPE_JWT)) {
                    return cookie.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }

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
     * 先从请求中获取(暂时没有应用场景), 获取不到则从 cookie 中获取
     *
     * @param httpServletRequest
     * @return
     */
    public static String getCompactJwt(HttpServletRequest httpServletRequest) {
        // 从 request 中获取 jwt
        String compactJws = httpServletRequest.getParameter(AuthConstants.TYPE_JWT);
        // request 中获取不到,就从 cookies 中获取 jwt
        if (StringUtils.isEmpty(compactJws)) {
            Cookie[] cookies = httpServletRequest.getCookies();
            compactJws = getJwtFromCookie(cookies);
        }
        return compactJws;
    }

    /**
     * 根据环境获取重定向地址
     *
     * @return
     */
    public static String getAdLoginUrl() {
        return AuthConstants.LOGIN_ADDRESS;
    }
}
