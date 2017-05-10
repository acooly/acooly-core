
package com.acooly.module.sso.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.sso.dic.AuthResult;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shuijing
 */
@Slf4j
public class DefaultLoginAuth extends AbstractLoginJwtAuthProcessor<AuthResult> {


    @Override
    public AuthResult loginAuthentication(String authentication, String loginUrl,
                                          HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticationExist(authentication)) {
            if (!isLoginUrlExist(loginUrl)) {
                return AuthResult.LOGIN_URL_NULL;
            }
            if (!isDomainMatch(request.getRequestURL().toString(), loginUrl)) {
                return AuthResult.LOGIN_ERROR_DOMAIN;
            }
            return AuthResult.LOGIN_REDIRECT;
        }
        return validateAuthentication(request, authentication);
    }

    /**
     * jwt 验证篡改与过期
     */
    private AuthResult validateAuthentication(HttpServletRequest request, String authentication) {
        try {
            // 验证jwt是否被篡改
            Jwt jwt = JWTUtils.parseAuthentication(authentication);
            // 验证 jwt 是否过期
            if (!validateTimeout(jwt)) {
                // 将解析后的信息存入 request 属性中
                setRequestAttributes(request, jwt);

                return AuthResult.AUTHENTICATION_ACCESS;
            }
            return AuthResult.LOGIN_AUTHENTICATION_TIME_OUT;
        } catch (Exception e) {
            return AuthResult.AUTHENTICATION_TAMPER;
        }
    }

    private boolean isDomainMatch(String requestURL, String loginUrl) {
        URL url= null;
        try {
            url = new URL(loginUrl);
        } catch (MalformedURLException e) {
            log.error("登录地址格式有误",e);
        }
        String host = url.getHost();
        return requestURL.contains(host.replaceAll(".*\\.(?=.*\\.)", ""));
    }
}
