
package com.acooly.module.sso.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.shiro.cache.ShiroCacheManager;
import com.acooly.module.sso.dic.AuthResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.HttpServletSession;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shuijing
 */
@Slf4j
public class DefaultLoginAuth extends AbstractLoginJwtAuthProcessor<AuthResult> {

    private WebSecurityManager securityManager;

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
            Jwt<Header, Claims> jwt = JWTUtils.parseAuthentication(authentication);
            // 验证 jwt 是否过期
            if (!validateTimeout(jwt)) {
                // 将解析后的信息存入 request 属性中
                setRequestAttributes(request, jwt);
                //将subject绑定到线程
                bindSubjectToThread(jwt,request);

                return AuthResult.AUTHENTICATION_ACCESS;
            }
            return AuthResult.LOGIN_AUTHENTICATION_TIME_OUT;
        } catch (Exception e) {
            return AuthResult.AUTHENTICATION_TAMPER;
        }
    }

    private boolean isDomainMatch(String requestURL, String loginUrl) {
        URL url = null;
        try {
            url = new URL(loginUrl);
        } catch (MalformedURLException e) {
            log.error("登录地址格式有误", e);
        }
        String host = url.getHost();
        return requestURL.contains(host.replaceAll(".*\\.(?=.*\\.)", ""));
    }


    public WebSecurityManager getSecurityManager() {
        if (securityManager == null) {
            securityManager = ApplicationContextHolder.get().getBean(DefaultWebSecurityManager.class);
        }
        return securityManager;
    }

    private void bindSubjectToThread(Jwt<Header, Claims> jwt, HttpServletRequest request) throws IOException {
        SecurityManager securityManager = ThreadContext.getSecurityManager();
        if (securityManager == null) {
            ThreadContext.bind(getSecurityManager());
        }
        Subject conSubject = ThreadContext.getSubject();
        if (conSubject == null) {
            String userStr = (String) jwt.getBody().get(JWTUtils.CLAIMS_KEY_SUBJECT);
            User user = JsonMapper.nonEmptyMapper().getMapper().readValue(userStr, User.class);
            SimplePrincipalCollection simplePrincipal = new SimplePrincipalCollection(user, ShiroCacheManager.KEY_AUTHC);

            HttpSession httpSession = request.getSession(true);
            HttpServletSession shiroSession = null;
            if (httpSession != null) {
                shiroSession = new HttpServletSession(httpSession, request.getRemoteHost());
            }
            Subject subject = new Subject.Builder().sessionId(request.getSession().getId()).session(shiroSession).principals(simplePrincipal).authenticated(true).buildSubject();
            ThreadContext.bind(subject);
        }
    }

}
