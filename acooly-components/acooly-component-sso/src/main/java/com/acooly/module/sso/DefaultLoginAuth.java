
package com.acooly.module.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.sso.dic.AuthResult;
import io.jsonwebtoken.Jwt;

/**
 * @author shuijing
 */
public class DefaultLoginAuth extends AbstractLoginJwtAuthProcessor<AuthResult> {


    @Override
    public AuthResult loginAuthentication(String authentication, String loginUrl,
                                          HttpServletRequest request, HttpServletResponse response) {
        // String requestURL = request.getRequestURL().toString();
        if (!isAuthenticationExist(authentication)) {
            if (!isLoginUrlExist(loginUrl)) {
                return AuthResult.LOGIN_URL_NULL;
            }
//            if (!isDomainMatch(requestURL)) {
//                return AuthResult.LOGIN_ERROR_DOMAIN;
//            }
            return AuthResult.LOGIN_REDIRECT;
        }
        return validateAuthentication(request, authentication);
    }

    /**
     * jwt 信息验证(篡改与过期)
     *
     * @param request
     * @param authentication
     * @return
     */
    private AuthResult validateAuthentication(HttpServletRequest request, String authentication) {
        try {
            // 验证jwt是否被篡改
            Jwt jwt = parseAuthentication(authentication);
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

    private boolean isDomainMatch(String requestURL) {
        return requestURL.contains(JWTUtils.COOKIE_DOMAIN);
    }
}
