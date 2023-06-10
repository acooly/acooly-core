package com.acooly.core.utils.security;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Exceptions;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 加密和签名
 * 主要提供hmac-sha1签名及严重；AES加密和解密的常用模式工具类
 *
 * @author zhangpu
 * @date 2014-6-16
 */
public class Cryptos {

    private static final String AES = "AES";
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
    private static final String HMACSHA1 = "HmacSHA1";
    private static final String DEFAULT_ENCODE = "UTF-8";
    /**
     * RFC2401
     */
    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final int DEFAULT_IVSIZE = 16;

    /**
     * 安全的随机数源
     */
    private static SecureRandom random = new SecureRandom();

    // -- X509 -- //

    // -- HMAC-SHA1 funciton --//

    /**
     * 使用HMAC-SHA1进行消息签名（字节数组）
     *
     * @param input 原始输入字符数组
     * @param key   HMAC-SHA1密钥
     * @return 编码后的字节数组，长度为20字节.
     */
    public static byte[] hmacSha1(byte[] input, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1);
            Mac mac = Mac.getInstance(HMACSHA1);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 使用HMAC-SHA1进行消息签名（指定编码）
     *
     * @param input  原始输入字符
     * @param encode 字符编码
     * @param key    HMAC-SHA1密钥
     * @return Hex编码的结果，长度为40字符.
     */
    public static String hmacSha1(String input, String encode, String key) {
        byte[] inputBytes = null;
        byte[] keyBytes = null;
        try {
            inputBytes = input.getBytes(encode);
            keyBytes = key.getBytes(encode);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
        return Encodes.encodeHex(hmacSha1(inputBytes, keyBytes));
    }

    /**
     * 使用HMAC-SHA1进行消息签名（默认UTF-8编码）
     *
     * @param input 原始输入字符
     * @param key   HMAC-SHA1密钥
     * @return Hex编码的结果，长度为40字符.
     */
    public static String hmacSha1(String input, String key) {
        return hmacSha1(input, DEFAULT_ENCODE, key);
    }

    /**
     * 校验HMAC-SHA1签名是否正确（字节数组）.
     *
     * @param expected 已存在的签名
     * @param input    原始输入字符串
     * @param key      密钥
     * @return 校验结果，true：正确 or false：不正确
     */
    public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    public static boolean isMacValid(String expected, String input, String key) {
        try {
            return isMacValid(expected.getBytes(), input.getBytes(), key.getBytes());
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 生成HMAC-SHA1密钥
     * <p>
     * HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
     *
     * @return HMAC-SHA1密钥，
     */
    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    // -- AES funciton --//


    /**
     * 支持向量的AES加密全方法（字节数组）.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @return 加密后的字节数组
     */
    public static byte[] aesEncrypt(byte[] input, byte[] key, byte[] iv) {
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }


    /**
     * 使用AES加密（内置向量，字节数组）
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     * @return 加密后的字节数组
     */
    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }


    /**
     * AES字符串加密
     *
     * @param input  原始输入字符
     * @param keyHex 秘钥的Hex编码
     * @return 加密后的字节数组
     */
    public static byte[] aesEncrypt(String input, String keyHex) {
        return aesEncrypt(input.getBytes(), Encodes.decodeHex(keyHex));
    }

    /**
     * 使用AES解密字符串(字节数组输入)
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥
     * @return 解密后字符串原文
     */
    public static String aesDecrypt(byte[] input, byte[] key) {
        byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES解密字符串（字符串模式输入）
     *
     * @param input  Hex编码的加密字符串
     * @param keyHex 符合AES要求的密钥的Hex编码
     * @return 解密后字符串原文
     */
    public static String aesDecrypt(String input, String keyHex) {
        return aesDecrypt(input.getBytes(), Encodes.decodeHex(keyHex));
    }

    /**
     * 使用AES解密字符串（字节数组，指定向量）
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @return 解密后字符串原文
     */
    public static String aesDecrypt(byte[] input, byte[] key, byte[] iv) {
        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组,
     * <p>
     * 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 加/解密后的字节数组，根据模式来判断
     */
    public static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 指定向量的AES加密或解密无编码的原始字节数组,
     * <p>
     * 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量,一般为128位不可预测的随机数据
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 加/解密后的字节数组，根据模式来判断
     */
    public static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(mode, secretKey, ivSpec);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 生成默认长度（128位）AES随机密钥
     *
     * @return 128位的随机密钥
     */
    public static byte[] generateAesKey() {
        return generateAesKey(DEFAULT_AES_KEYSIZE);
    }

    /**
     * 生成指定长度的AES随机密钥
     *
     * @param keysize 随机密钥长度（可选：128,192,256位）
     * @return 指定位数的随机密钥
     */
    public static byte[] generateAesKey(int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 生成随机向量,默认大小为cipher.getBlockSize(), 16字节.
     *
     * @return 16字节的随机向量
     */
    public static byte[] generateIV() {
        byte[] bytes = new byte[DEFAULT_IVSIZE];
        random.nextBytes(bytes);
        return bytes;
    }

}
