/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.acooly.core.utils;

import org.apache.commons.lang3.Validate;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 消息摘要工具类（可选）
 * <p>
 * 支持SHA-1/MD5；
 * 推荐直接使用：`org.apache.commons.codec.digest.DigestUtils`工具类
 *
 * @author calvin
 * @see org.apache.commons.codec.digest.DigestUtils
 */
public class Digests {

    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "MD5";

    private static SecureRandom random = new SecureRandom();


    /**
     * 单次无盐SHA1摘要签名
     *
     * @param input 待签名的数据
     * @return 签名后的数据
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1, null, 1);
    }

    /**
     * 单次SHA1摘要签名
     *
     * @param input 待签名的数据
     * @param salt  盐值
     * @return 签名后的数据
     */
    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1, salt, 1);
    }

    /**
     * SHA1摘要签名(根方法)
     *
     * @param input      待签名的数据
     * @param salt       盐值
     * @param iterations 迭代次数
     * @return 签名后的数据
     */
    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * SHA1流式摘要签名
     * <p>
     * 注意：签名后input未关闭
     *
     * @param input 待摘要签名的数据
     * @return 签名后的摘要
     */
    public static byte[] sha1(InputStream input) {
        return digest(input, SHA1);
    }


    /**
     * MD5流式摘要签名
     * <p>
     * 注意：签名后input未关闭
     *
     * @param input 待摘要签名的数据
     * @return 签名后的摘要
     */
    public static byte[] md5(InputStream input) {
        return digest(input, MD5);
    }

    /**
     * 生成随机盐
     * <p>
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     * @return byte数组
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 通用流式摘要签名
     *
     * @param input     待签名的数据
     * @param algorithm 签名算法，支持SHA-1和MD5
     * @param salt      盐值
     * @return 签名后的数据
     * @see #SHA1
     * @see #MD5
     */
    public static byte[] digest(InputStream input, String algorithm, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                messageDigest.update(salt);
            }
            // 8K缓冲区
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }

            return messageDigest.digest();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 通用流式摘要签名(不添加盐)
     *
     * @param input     待签名的数据
     * @param algorithm 签名算法，支持SHA-1和MD5
     * @return 签名后的数据
     */
    public static byte[] digest(InputStream input, String algorithm) {
        return digest(input, algorithm, null);
    }

    /**
     * 通用摘要签名
     * (根方法)
     *
     * @param input      待签名的数据
     * @param algorithm  签名算法，支持SHA-1和MD5
     * @param salt       盐值
     * @param iterations 迭代次数，循环签名次数
     * @return 签名后的数据
     * @see #SHA1
     * @see #MD5
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, Integer iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            if (iterations != null && iterations > 0) {
                for (int i = 1; i < iterations; i++) {
                    digest.reset();
                    result = digest.digest(result);
                }
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

}
