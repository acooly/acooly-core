
package com.acooly.module.sso;

import javax.servlet.http.HttpServletRequest;

import com.acooly.module.sso.dic.AuthConstants;
import com.acooly.module.sso.jwtt.JwtSigningKeyResolver;
import org.apache.commons.lang3.StringUtils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

/**
 * @author shuijing
 */
public abstract class AbstractLoginJwtAuthProcessor<T> implements LoginAuthProcessor<T> {

    /**
     * 解密并验证信息有无被篡改
     *
     * @param authentication
     * @return
     * @throws Exception
     */
    public static Jwt<Header, Claims> parseAuthentication(String authentication) throws Exception {
        return Jwts.parser().setSigningKeyResolver(new JwtSigningKeyResolver()).parse(authentication);
    }

    /**
     * 将解析后的信息存入 request 属性中
     *
     * @param request
     * @param jwt
     */
    protected void setRequestAttributes(HttpServletRequest request, Jwt<Header, Claims> jwt) {
        Claims claims = jwt.getBody();
        request.setAttribute(AuthConstants.KEY_ISS, claims.get(AuthConstants.CLAIMS_KEY_ISS));
        request.setAttribute(AuthConstants.KEY_SUB_NAME, claims.get(AuthConstants.CLAIMS_KEY_SUB));
        request.setAttribute(AuthConstants.KEY_AUD, claims.get(AuthConstants.CLAIMS_KEY_AUD));
        request.setAttribute(AuthConstants.KEY_IAT, claims.get(AuthConstants.CLAIMS_KEY_IAT));
        request.setAttribute(AuthConstants.KEY_EXP, claims.get(AuthConstants.CLAIMS_KEY_EXP));
    }

    /**
     * 验证 jwt 是否过期
     *
     * @param jwt
     * @return
     */
    protected boolean validateTimeout(Jwt<Header, Claims> jwt) {
        long expTime = Long.valueOf(String.valueOf(jwt.getBody().get(AuthConstants.CLAIMS_KEY_EXP)));
        return (System.currentTimeMillis() >= expTime);
    }

    /**
     * 验证 jwt 信息是否存在
     *
     * @param authentication
     * @return
     */
    protected boolean isAuthenticationExist(String authentication) {
        if (StringUtils.isNotBlank(authentication)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * LoginUrl 是否配置
     *
     * @param loginUrl
     * @return
     */
    protected boolean isLoginUrlExist(String loginUrl) {
        if (StringUtils.isNotBlank(loginUrl)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
