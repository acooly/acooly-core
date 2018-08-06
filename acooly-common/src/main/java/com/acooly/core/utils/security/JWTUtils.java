package com.acooly.core.utils.security;

import com.acooly.core.utils.Servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuijing
 */
@Slf4j
public class JWTUtils {

    public static final String KEY_TARGETURL = "targetUrl";
    /**
     * type Json Web Token
     */
    public static final String TYPE_JWT = "jwt";

    /**
     * token 签发者 session Attribute
     */
    public static final String KEY_ISS = "keyIss";

    /**
     * token 所面向的用户名
     */
    public static final String KEY_SUB_NAME = "keySubName";

    /**
     * 接收 token 的一方
     */
    public static final String KEY_AUD = "keyAud";

    /**
     * token 签发时间
     */
    public static final String KEY_IAT = "keyIat";

    /**
     * token 失效时间
     */
    public static final String KEY_EXP = "keyExp";

    /**
     * jwt 加密密钥
     */
    public static final String SIGN_KEY = "ssoSecretlkj";

    /**
     * JWT 签发者 key
     */
    public static final String CLAIMS_KEY_ISS = "iss";

    /**
     * JWT 签发时间 key
     */
    public static final String CLAIMS_KEY_IAT = "iat";

    /**
     * JWT 过期时间 key
     */
    public static final String CLAIMS_KEY_EXP = "exp";

    /**
     * 用户名 key
     */
    public static final String CLAIMS_KEY_SUB = "sub";

    /**
     * user
     */
    public static final String CLAIMS_KEY_SUBJECT = "subject";

    /**
     * 接收地址 key
     */
    public static final String CLAIMS_KEY_AUD = "aud";

    /**
     * 规范类型 key
     */
    public static final String HEADER_KEY_TYP = "typ";

    /**
     * 加密算法 key
     */
    public static final String HEADER_KEY_ALG = "alg";

    /**
     * 规范类型
     */
    public static final String PROTOCOL_TYPE_JWT = "jwt";

    public static final char SEPARATOR_CHAR = '.';
    /**
     * jwt 过期时间，单位分钟
     */
    public static final Long JWT_EXP_TIME = 7 * 24 * 60L;
    public static JwtSigningKeyResolver jwtSigningKeyResolver = new JwtSigningKeyResolver();
    /**
     * header
     */
    public static Map headerMap;
    /**
     * JWT 签发者 value
     */
    private static String iss = "acooly";
    /**
     * JWT 接收方 (暂无用)
     */
    private static String aud = "boss";
    private static ObjectMapper objectMapper;

    static {
        headerMap = new HashMap<>();
        headerMap.put(HEADER_KEY_TYP, PROTOCOL_TYPE_JWT);
        headerMap.put(HEADER_KEY_ALG, SignatureAlgorithm.HS256.getValue());
        objectMapper = new ObjectMapper();
    }

    public static String createJwt(String sub, String subjectStr) {
        Date iat = new Date();
        // 实效时间为 120分钟
        Date expTime = new Date(iat.getTime() + JWT_EXP_TIME * 60 * 1000);
        Map claims = new HashMap<>();
        claims.put(CLAIMS_KEY_ISS, iss);
        claims.put(CLAIMS_KEY_SUB, sub);
        claims.put(CLAIMS_KEY_AUD, aud);
        claims.put(CLAIMS_KEY_IAT, iat);
        claims.put(CLAIMS_KEY_EXP, expTime);
        claims.put(CLAIMS_KEY_SUBJECT, subjectStr);
        String compactJws =
                Jwts.builder()
                        .setHeader(headerMap)
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, SIGN_KEY.getBytes())
                        .compact();
        return compactJws;
    }

    public static String refreshJwt(Jwt<Header, Claims> jws) {
        Date expTime = new Date((System.currentTimeMillis() + JWT_EXP_TIME * 60 * 1000));
        jws.getBody().put(CLAIMS_KEY_EXP, expTime);
        String newJws =
                Jwts.builder()
                        .setHeader(headerMap)
                        .setClaims(jws.getBody())
                        .signWith(SignatureAlgorithm.HS256, SIGN_KEY.getBytes())
                        .compact();
        //更新jwt
        JWTUtils.removeCookie(JWTUtils.TYPE_JWT, JWTUtils.getDomainName());
        JWTUtils.addJwtCookie(Servlets.getResponse(), newJws, JWTUtils.getDomainName());
        return newJws;
    }

    /**
     * 根据jwt加密串获得 Claims
     *
     * @param jwt
     * @return
     */
    public static Claims getClaims(String jwt) {

        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");

        String base64UrlEncodedPayload = null;

        int delimiterCount = 0;

        StringBuilder sb = new StringBuilder(128);

        for (char c : jwt.toCharArray()) {

            if (c == SEPARATOR_CHAR) {

                String token = Strings.clean(sb.toString());

                if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb = new StringBuilder(128);
            } else {
                sb.append(c);
            }
        }

        String payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);

        Claims claims = null;

        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') {
            Map<String, Object> claimsMap = readValue(payload);
            claims = new DefaultClaims(claimsMap);
        }
        return claims;
    }

    @SuppressWarnings("unchecked")
    protected static Map<String, Object> readValue(String val) {
        try {
            return objectMapper.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * 解密并验证信息有无被篡改
     *
     * @param compactJws
     * @return
     * @throws Exception
     */
    public static Jwt parseJws(String compactJws, final String secretKey) throws Exception {
        return Jwts.parser()
                .setSigningKeyResolver(
                        new SigningKeyResolverAdapter() {
                            @Override
                            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                                SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
                                byte[] keyBytes = this.resolveSigningKeyBytes(header, claims);
                                return new SecretKeySpec(keyBytes, alg.getJcaName());
                            }

                            @Override
                            public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                                return secretKey.getBytes();
                            }
                        })
                .parse(compactJws);
    }

    /**
     * 解密并验证信息有无被篡改
     *
     * @param authentication
     * @return
     * @throws Exception
     */
    public static Jwt<Header, Claims> parseAuthentication(String authentication) throws Exception {
        return Jwts.parser().setSigningKeyResolver(jwtSigningKeyResolver).parse(authentication);
    }

    /**
     * 验证 jwt 是否过期
     *
     * @param jwt
     * @return
     */
    public static boolean validateTimeout(Jwt<Header, Claims> jwt) {
        long expTime = Long.valueOf(String.valueOf(jwt.getBody().get(JWTUtils.CLAIMS_KEY_EXP)));
        return (System.currentTimeMillis() >= expTime);
    }

    /**
     * 验证 jwt 信息
     *
     * @param jwt
     * @return
     */
    public static boolean validateJwt(String jwt, String sub) {
        if (sub == null) return false;
        Claims claims = getClaims(jwt);
        String subject = claims.getSubject();
        long expTime = claims.getExpiration().getTime();
        return (System.currentTimeMillis() <= expTime) && sub.equals(subject);
    }

    /**
     * 从 cookie 中获取 jwt
     *
     * @param cookies
     * @return
     */
    public static String getJwtFromCookie(Cookie[] cookies) {
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JWTUtils.TYPE_JWT)) {
                    return cookie.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * cookie 中添加 jwt
     *
     * @param response
     * @param jwtValue
     */
    public static void addJwtCookie(HttpServletResponse response, String jwtValue, String domain) {
        Cookie cookie = new Cookie(JWTUtils.TYPE_JWT, jwtValue);
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    public static void removeCookie(String key, String domain) {
        Cookie[] cookies = Servlets.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), key)) {
                cookie.setHttpOnly(Boolean.TRUE);
                cookie.setPath("/");
                cookie.setDomain(domain);
                cookie.setMaxAge(0);
                Servlets.getResponse().addCookie(cookie);
            }
        }
    }

    public static String getDomainName() {
        return Servlets.getRequest().getServerName().replaceAll(".*\\.(?=.*\\.)", "");
    }

    private static class JwtSigningKeyResolver extends SigningKeyResolverAdapter {

        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
            byte[] keyBytes = this.resolveSigningKeyBytes(header, claims);
            return new SecretKeySpec(keyBytes, alg.getJcaName());
        }

        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
            return JWTUtils.SIGN_KEY.getBytes();
        }
    }
}
