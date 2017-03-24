package com.acooly.module.sso.dic;


/**
 * @author shuijing
 */
public interface JWTConstants {
    /**
     * 重定向登录地址
     */
    public final static String CLAIMS_KEY_LOGIN_URL = "loginUrl";

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


}
