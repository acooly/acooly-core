package com.acooly.module.defence.url;

/**
 * @author qiubo@yiji.com
 */
public interface UrlSecurityService {
    /**
     * 加密
     */
    String encrypt(String text);

    /**
     * 解密
     *
     * @throws IllegalArgumentException 密文不安全时抛出异常
     */
    String decrypt(String encrypted) throws IllegalArgumentException;
}
