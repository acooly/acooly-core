
package com.acooly.module.sso.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.shiro.cache.ShiroCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
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
public abstract class AbstractLoginJwtAuthProcessor<T> implements LoginAuthProcessor<T> {

    private WebSecurityManager securityManager;

    private String baseLoginDomain = null;


    protected boolean isDomainMatch(String requestURL, String loginUrl) {
        if (baseLoginDomain == null) {
            baseLoginDomain = getRootDomain(loginUrl);
        }
        return requestURL.contains(baseLoginDomain);
    }

    private String getRootDomain(String loginUrl) {
        URL url = null;
        try {
            url = new URL(loginUrl);
        } catch (MalformedURLException e) {
            log.error("登录地址格式有误", e);
        }
        String host = url.getHost();
        return host.replaceAll(".*\\.(?=.*\\.)", "");
    }


    public WebSecurityManager getSecurityManager() {
        if (securityManager == null) {
            securityManager = ApplicationContextHolder.get().getBean(DefaultWebSecurityManager.class);
        }
        return securityManager;
    }

    protected void bindSubjectToThread(Jwt<Header, Claims> jwt, HttpServletRequest request) throws IOException {
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

    /**
     * 将解析后的信息存入 request 属性中
     */
    protected void setRequestAttributes(HttpServletRequest request, Jwt<Header, Claims> jwt) {
        Claims claims = jwt.getBody();
        request.setAttribute(JWTUtils.KEY_ISS, claims.get(JWTUtils.CLAIMS_KEY_ISS));
        request.setAttribute(JWTUtils.KEY_SUB_NAME, claims.get(JWTUtils.CLAIMS_KEY_SUB));
        request.setAttribute(JWTUtils.KEY_AUD, claims.get(JWTUtils.CLAIMS_KEY_AUD));
        request.setAttribute(JWTUtils.KEY_IAT, claims.get(JWTUtils.CLAIMS_KEY_IAT));
        request.setAttribute(JWTUtils.KEY_EXP, claims.get(JWTUtils.CLAIMS_KEY_EXP));
    }

    /**
     * 验证 jwt 信息是否存在
     */
    protected boolean isAuthenticationExist(String authentication) {
        if (StringUtils.isNotBlank(authentication)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * LoginUrl 是否配置
     */
    protected boolean isLoginUrlExist(String loginUrl) {
        if (StringUtils.isNotBlank(loginUrl)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
