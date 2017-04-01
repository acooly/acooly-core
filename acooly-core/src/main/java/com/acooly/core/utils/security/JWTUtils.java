package com.acooly.core.utils.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;
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
public class JWTUtils {

    public final static String KEY_TARGETURL = "targetUrl";
    /**
     * type Json Web Token
     */
    public final static String TYPE_JWT = "jwt";

    /**
     * token 签发者 session Attribute
     */
    public final static String KEY_ISS = "keyIss";

    /**
     * token 所面向的用户名
     */
    public final static String KEY_SUB_NAME = "keySubName";

    /**
     * 接收 token 的一方
     */
    public final static String KEY_AUD = "keyAud";

    /**
     * token 签发时间
     */
    public final static String KEY_IAT = "keyIat";

    /**
     * token 失效时间
     */
    public final static String KEY_EXP = "keyExp";

    /**
     * jwt 加密密钥
     */
    public final static String SIGN_KEY = "ssoSecret";

    /**
     * JWT 签发者 key
     */
    public final static String CLAIMS_KEY_ISS = "iss";

    /**
     * JWT 签发时间 key
     */
    public final static String CLAIMS_KEY_IAT = "iat";

    /**
     * JWT 过期时间 key
     */
    public final static String CLAIMS_KEY_EXP = "exp";

    /**
     * 用户名 key
     */
    public final static String CLAIMS_KEY_SUB = "sub";

    /**
     * 接收地址 key
     */
    public final static String CLAIMS_KEY_AUD = "aud";

    /**
     * 规范类型 key
     */
    public final static String HEADER_KEY_TYP = "typ";

    /**
     * 加密算法 key
     */
    public final static String HEADER_KEY_ALG = "alg";


    /**
     * 只有 .acooly.com 子域名才支持 sso 登录
     */
    public final static String COOKIE_DOMAIN = ".acooly.com";


    /**
     * 规范类型
     */
    public final static String PROTOCOL_TYPE_JWT = "jwt";
    /**
     * 加密类型 value
     */
    public final static String ALG_SH256 = "SH256";
    public static final char SEPARATOR_CHAR = '.';


    /**
     * JWT 签发者 value
     */
    private static String iss = "acooly";
    /**
     * JWT 接收方 (暂无用)
     */
    private static String aud = "boss";
    /**
     * header
     */
    private static Map headerMap;
    private static ObjectMapper objectMapper;

    static {
        headerMap = new HashMap<>();
        headerMap.put(HEADER_KEY_TYP, PROTOCOL_TYPE_JWT);
        headerMap.put(HEADER_KEY_ALG, ALG_SH256);
        objectMapper = new ObjectMapper();
    }


    public static String createJwt(String sub, String signKey) {
        Date iat = new Date();
        // 实效时间为 30分钟
        Date expTime = new Date(iat.getTime() + 30 * 60 * 60 * 1000);
        Map claims = new HashMap<>();
        claims.put(CLAIMS_KEY_ISS, iss);
        claims.put(CLAIMS_KEY_SUB, sub);
        claims.put(CLAIMS_KEY_AUD, aud);
        claims.put(CLAIMS_KEY_IAT, iat);
        claims.put(CLAIMS_KEY_EXP, expTime);
        String compactJws = Jwts.builder().setHeader(headerMap).setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, signKey.getBytes()).compact();
        return compactJws;
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
        return Jwts.parser().setSigningKeyResolver(new SigningKeyResolverAdapter() {
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
        }).parse(compactJws);
    }

    /**
     * 验证 jwt 信息
     *
     * @param jwt
     * @return
     */
    public static boolean validateJwt(String jwt, String sub) {
        if (sub == null)
            return false;
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
    public static void addJwtCookie(HttpServletResponse response, String jwtValue) {
        Cookie cookie = new Cookie(JWTUtils.TYPE_JWT, jwtValue);
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setPath("/");
        //TODO 域名可变
        cookie.setDomain("acooly.com");
        response.addCookie(cookie);
    }

}
