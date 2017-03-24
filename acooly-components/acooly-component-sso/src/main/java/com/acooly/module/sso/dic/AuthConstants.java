
package com.acooly.module.sso.dic;


/**
 * @author shuijing
 */
public class AuthConstants implements JWTConstants {

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
     * filter开关 false表示关闭
     */
    public final static String KEY_OPEN = "acooly.jwtfilter.open";


    /**
     * 不需要拦截处理的静态资源 key
     */
    public final static String KEY_STATIC_RESOURCE = "acooly.jwtfilter.resource.suffix";

    public static final String LOGIN_ADDRESS = "http://sso.acooly.com:8080/";
}
