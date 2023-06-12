/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年4月26日
 *
 */
package com.acooly.core.utils.security;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * RAS 非对称加密工具类
 *
 * <p>
 * 基础说明：
 * <li>不管明文长度是多少，RSA 生成的密文长度总是固定的。
 * <li>明文长度不能超过密钥长度。比如 Java 默认的RSA加密实现不允许明文长度超过密钥长度减去 11(单位是字节，也就是 byte)。也就是说，如果我们定义的密钥(我们可以通过
 * java.security.KeyPairGenerator.initialize(int keysize) 来定义密钥长度)长度为1024(单位是位，也就是
 * bit)，生成的密钥长度就是 1024位 / 8位/字节 = 128字节，那么我们需要加密的明文长度不能超过 128字节 - 11 字节 = 117字节。也就是说，我们最大能将 117
 * 字节长度的明文进行加密，否则会出问题(抛诸如 javax.crypto.IllegalBlockSizeException: Data must not be longer than
 * 53 bytes 的异常)。
 * <li>BC(Provider --> Security.addProvider(new BouncyCastleProvider());) 提供的加密算法能够支持到的 RSA
 * 明文长度最长为密钥长度。
 * </p>
 * <br/>
 * <p>
 * 本工具类提供了如下功能：
 * <li>生成RSA秘钥对
 * <li>从keystore,pem,x509证书,bytes,base64,Hex等多种数据格式中加载公私秘钥对象
 * <li>数字签名：使用RSA私钥签名,公钥验签
 * <li>加密解密：使用RSA公钥加密,私钥解密(分段加密解密)
 * </p>
 *
 * @author zhangpu
 * @date 2016年4月26日
 */
public class RSA {

    /**
     * 算法和编码默认值
     */
    public static final String DEFAULT_KEY_ALGO = "RSA";
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * RAS标准算法名称：SHA1withRSA,SHA256withRSA
     */
    public static final String SIGN_ALGO_SHA1 = "SHA1withRSA";
    public static final String SIGN_ALGO_SHA2 = "SHA256withRSA";
    public static final String SIGN_ALGO_MD5 = "MD5withRSA";

    /**
     * 存储秘钥对的keystore类型，JKS（java体系）或PKCS12（通用性好，默认）
     */
    public static final String KEY_STORE_JKS = "JKS";
    public static final String KEY_STORE_PKCS12 = "PKCS12";
    public static final String DEFAULT_KEY_STORE = KEY_STORE_PKCS12;

    // ******** 非对称秘钥数字签名（私钥）/验签（公钥） ********

    /**
     * 私钥签名(字节数组方式传值，主方法)
     * 数字签名：私钥签名，公钥验签。这里是第一步：使用秘钥对的私钥进行签名，然后使用公钥进行验签(verify)
     *
     * @param dataBytes          明文数据bytes
     * @param privateKey         私钥 参考 loadPrivateKey方法
     * @param signatureAlgorithm 签名算法 如：SHA1withRSA，SHA256withRSA，MD5withRSA
     * @return 签名的二进制结果
     * @see #verify(byte[], byte[], PublicKey, String)
     * @see #SIGN_ALGO_SHA1
     * @see #SIGN_ALGO_SHA2
     * @see #SIGN_ALGO_MD5
     */
    public static byte[] sign(@NotNull byte[] dataBytes, @NotNull PrivateKey privateKey, @Null String signatureAlgorithm) {
        String sa = Strings.isBlankDefault(signatureAlgorithm, SIGN_ALGO_SHA1);
        try {
            Signature signature = Signature.getInstance(sa);
            signature.initSign(privateKey);
            signature.update(dataBytes);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("sign with " + sa + " fail:" + e.getMessage());
        }
    }

    /**
     * 私钥签名（字符串方式）
     *
     * @param data               待签名数据
     * @param privateKey         私钥对象
     * @param signatureAlgorithm 签名算法
     * @param dataCharset        待签名数据字符集
     * @return 签名数据的Hex（16进制）编码字符串
     */
    public static String signHex(
            @NotNull String data, @NotNull PrivateKey privateKey,
            @Null String signatureAlgorithm, @Null String dataCharset) {
        byte[] result = sign(getBytes(data, dataCharset), privateKey, signatureAlgorithm);
        return Encodes.encodeHex(result);
    }

    /**
     * 私钥签名（字符串方式,默认算法和编码）
     * 默认算法：SHA1withRSA
     * 默认编码：UTF-8
     *
     * @param data       待签名数据
     * @param privateKey 私钥对象
     * @return 签名数据的Hex（16进制）编码字符串
     */
    public static String signHex(@NotNull String data, @NotNull PrivateKey privateKey) {
        return signHex(data, privateKey, null, null);
    }

    /**
     * 私钥签名BASE64数据
     *
     * @param data               base64编码的待签名数据
     * @param privateKey         私钥对象
     * @param signatureAlgorithm 签名算法
     * @param dataCharset        待签名数据字符集
     * @return 签名数据的BASE64编码字符串
     * @see #loadPrivateKey(File)
     */
    public static String signBase64(@NotNull String data, @NotNull PrivateKey privateKey,
                                    @Null String signatureAlgorithm, @Null String dataCharset) {
        byte[] result = sign(getBytes(data, dataCharset), privateKey, signatureAlgorithm);
        return Encodes.encodeBase64(result);
    }

    /**
     * 私钥签名，输出BASE64数据（默认算法和编码）
     *
     * @param data       base64编码的待签名数据
     * @param privateKey 私钥对象
     * @return 签名数据的BASE64编码字符串
     */
    public static String signBase64(@NotNull String data, @NotNull PrivateKey privateKey) {
        return signBase64(data, privateKey, null, null);
    }


    /**
     * 公钥验签（字节数组，主方法）
     *
     * @param data               明文数据bytes
     * @param signData           签名数据bytes
     * @param publicKey          公钥 参考 loadPublicKey(...)方法
     * @param signatureAlgorithm 签名算法
     * @return 是否验签通过，true：通过，false：不通过
     * @see #loadPublicKey(File)
     */
    public static boolean verify(byte[] data, byte[] signData, PublicKey publicKey, String signatureAlgorithm) {
        String sa = Strings.isBlankDefault(signatureAlgorithm, SIGN_ALGO_SHA1);
        try {
            Signature signature = Signature.getInstance(sa);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signData);
        } catch (Exception e) {
            throw new RuntimeException("verify with " + sa + " fail." + e.getMessage());
        }
    }

    /**
     * 公钥验签Base64数据
     *
     * @param text               明文数据
     * @param signBase64         签名数据的BASE64编码字符串
     * @param publicKey          公钥对象
     * @param signatureAlgorithm 签名算法
     * @param charset            明文数据字符集
     * @return 是否验签通过，true：通过，false：不通过
     */
    public static boolean verifyBase64(
            String text, String signBase64, PublicKey publicKey,
            String signatureAlgorithm, String charset) {
        byte[] data = getBytes(text, charset);
        byte[] signData = Encodes.decodeBase64(signBase64);
        return verify(data, signData, publicKey, signatureAlgorithm);
    }

    /**
     * 公钥验签Base64数据（默认算法和编码）
     *
     * @param text       明文数据
     * @param signBase64 签名数据的BASE64编码字符串
     * @param publicKey  公钥对象
     * @return 是否验签通过，true：通过，false：不通过
     */
    public static boolean verifyBase64(String text, String signBase64, PublicKey publicKey) {
        return verifyBase64(text, signBase64, publicKey, null, null);
    }

    /**
     * 公钥验签Hex数据
     *
     * @param text               明文数据
     * @param signHex            签名数据的Hex（16进制）编码字符串
     * @param publicKey          公钥对象
     * @param signatureAlgorithm 签名算法
     * @param charset            明文数据字符集
     * @return 是否验签通过，true：通过，false：不通过
     */
    public static boolean verifyHex(String text, String signHex, PublicKey publicKey, String signatureAlgorithm, String charset) {
        byte[] data = getBytes(text, charset);
        byte[] signData = Encodes.decodeHex(signHex);
        return verify(data, signData, publicKey, signatureAlgorithm);
    }

    /**
     * 公钥验签Hex数据（默认算法和编码）
     *
     * @param text      明文数据
     * @param signHex   签名数据的Hex（16进制）编码字符串
     * @param publicKey 公钥对象
     * @return 是否验签通过，true：通过，false：不通过
     */
    public static boolean verifyHex(String text, String signHex, PublicKey publicKey) {
        return verifyHex(text, signHex, publicKey, null, null);
    }


    // ******** 非对称秘钥加（公钥）解（私钥）密 ********

    /**
     * 公钥加密（字节数组，主方法）
     *
     * @param plainBytes 明文bytes
     * @param publicKey  公钥对象
     * @return 密文bytes
     * @see #loadPrivateKey(File)
     */
    public static byte[] encryptByPublicKey(byte[] plainBytes, PublicKey publicKey) {
        ByteArrayOutputStream out = null;
        try {
            // Cipher在非对称加密场景，没有必要自己做thread cache，JCE实现的provider默认做了~
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = plainBytes.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
            int plainLength = keyLength - 11;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > plainLength) {
                    cache = cipher.doFinal(plainBytes, offSet, plainLength);
                } else {
                    cache = cipher.doFinal(plainBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * plainLength;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException("publicKey encrypt fail:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 公钥加密字符串数据，输出base64
     * 输入为制定字符集的字符串，输出位Base64编码的字符串
     *
     * @param plainText 字符串明文
     * @param publicKey 公钥对象
     * @param charset   字符串明文的字符集
     * @return Base64编码的密文字符串
     */
    public static String encryptByPublicKeyBase64(
            String plainText, PublicKey publicKey, String charset) {
        return Encodes.encodeBase64(encryptByPublicKey(getBytes(plainText, charset), publicKey));
    }

    /**
     * 公钥加密字符串数据，输出base64（默认字符集，UTF-8）
     *
     * @param plainText 字符串明文
     * @param publicKey 公钥对象
     * @return Base64编码的密文字符串
     * @see #encryptByPublicKeyBase64(String, PublicKey, String)
     */
    public static String encryptByPublicKeyBase64(String plainText, PublicKey publicKey) {
        return encryptByPublicKeyBase64(plainText, publicKey, DEFAULT_CHARSET);
    }

    /**
     * 公钥加密字符串数据，输出Hex
     *
     * @param plainText 字符串明文
     * @param publicKey 公钥对象
     * @param charset   字符串明文的字符集
     * @return Hex编码的密文字符串
     */
    public static String encryptByPublicKeyHex(
            String plainText, PublicKey publicKey, String charset) {
        return Encodes.encodeHex(encryptByPublicKey(getBytes(plainText, charset), publicKey));
    }

    /**
     * 公钥加密字符串数据，输出Hex（默认字符集，UTF-8）
     *
     * @param plainText 字符串明文
     * @param publicKey 公钥对象
     * @return Hex编码的密文字符串
     */
    public static String encryptByPublicKeyHex(String plainText, PublicKey publicKey) {
        return encryptByPublicKeyHex(plainText, publicKey, DEFAULT_CHARSET);
    }

    /**
     * 私钥解密(字节数组，主方法)
     *
     * @param encryptedBytes 密文数据bytes
     * @param privateKey     私钥对象
     * @return 明文数据bytes
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedBytes, PrivateKey privateKey) {
        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = encryptedBytes.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > keyLength) {
                    cache = cipher.doFinal(encryptedBytes, offSet, keyLength);
                } else {
                    cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * keyLength;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException("privateKey decrypt fail:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 私钥解密字符串数据，输入base64
     *
     * @param encryptedBase64 base64格式的密文数据，通过`encryptByPublicKeyBase64`方法获取的加密数据
     * @param privateKey      私钥对象
     * @param charset         字符串明文的字符集
     * @return 明文字符串
     * @see #encryptByPublicKeyBase64(String, PublicKey)
     */
    public static String decryptByPrivateKeyBase64(
            String encryptedBase64, PrivateKey privateKey, String charset) {
        byte[] plainBytes = decryptByPrivateKey(Encodes.decodeBase64(encryptedBase64), privateKey);
        try {
            return new String(plainBytes, Strings.isBlankDefault(charset, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符集:" + e.getMessage());
        }
    }

    /**
     * 私钥解密字符串数据，输入Hex
     *
     * @param encryptedHex Hex格式的密文数据，通过`encryptByPublicKeyHex`方法获取的加密数据
     * @param privateKey   私钥对象
     * @param charset      字符串明文的字符集
     * @return 明文字符串
     * @see #encryptByPublicKeyHex(String, PublicKey)
     */
    public static String decryptByPrivateKeyHex(
            String encryptedHex, PrivateKey privateKey, String charset) {
        byte[] plainBytes = decryptByPrivateKey(Encodes.decodeHex(encryptedHex), privateKey);
        try {
            return new String(plainBytes, Strings.isBlankDefault(charset, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符集:" + e.getMessage());
        }
    }

    // **************** 秘钥管理 **************** //

    /**
     * 通过keystore方式加载私钥
     *
     * @param keystoreUri      秘钥库资源路径，采用spring资源管理模式，支持classpath:,file:和http等
     * @param keystoreType     秘钥库类型，可选JPKCS12（默认）或JKS
     * @param keystorePassword 秘钥库密码
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKeyFromKeyStore(String keystoreUri, String keystoreType, String keystorePassword) {
        InputStream in = null;
        try {
            KeyStore keyStore = loadKeystore(keystoreUri, keystoreType, keystorePassword);
            Enumeration<String> enumeration = keyStore.aliases();
            String keyAlias = null;
            if (enumeration.hasMoreElements()) {
                keyAlias = enumeration.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, keystorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载私钥失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 通过私钥数据（字节数组）加载私钥对象
     *
     * @param privateKeyBytes 秘钥的二进制格式
     * @param keyAlgorithm    默认：RSA（常量DEFAULT_KEY_ALGO）
     * @param providerName    可以为空，默认：BC
     * @return 私钥对象
     * @see #DEFAULT_KEY_ALGO
     */
    public static PrivateKey loadPrivateKey(byte[] privateKeyBytes, String keyAlgorithm, String providerName) {
        String algorithm = Strings.isBlankDefault(keyAlgorithm, DEFAULT_KEY_ALGO);
        try {
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = null;
            if (Strings.isBlank(providerName)) {
                keyFactory = KeyFactory.getInstance(algorithm);
            } else {
                keyFactory = KeyFactory.getInstance(algorithm, providerName);
            }
            // 取私钥匙对象
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new RuntimeException("load privateKey fail. " + e.getMessage());
        }
    }

    /**
     * 通过base64数据加载私钥
     *
     * @param privateKeyBase64 秘钥为BASE64编码
     * @param keyAlgorithm     如：RSA
     * @param providerName     可以为空，如：BC
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(String privateKeyBase64, String keyAlgorithm, String providerName) {
        byte[] privateKeyBytes = Encodes.decodeBase64(privateKeyBase64);
        return loadPrivateKey(privateKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * 通过私钥文件加载私钥
     *
     * @param privateKeyFile 秘钥文件（内容为BASE64）
     * @param keyAlgorithm   如：RSA
     * @param providerName   可以为空，BC
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(File privateKeyFile, String keyAlgorithm, String providerName) {
        byte[] privateKeyBytes = null;
        try {
            privateKeyBytes = Encodes.decodeBase64(FileUtils.readFileToString(privateKeyFile, "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("加载秘钥文件内容失败:" + e.getMessage());
        }
        return loadPrivateKey(privateKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * 通过私钥文件加载私钥（默认算法）
     *
     * @param privateKeyFile 秘钥文件（内容为BASE64）
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(File privateKeyFile) {
        return loadPrivateKey(privateKeyFile, null, null);
    }

    /**
     * 加载公钥（从bytes字节数组）
     *
     * @param publicKeyBytes 公钥bytes
     * @param keyAlgorithm   算法，如：RSA
     * @param providerName   可为空
     * @return 公钥对象
     */
    public static PublicKey loadPublicKey(
            byte[] publicKeyBytes, String keyAlgorithm, String providerName) {
        String algorithm = Strings.isBlankDefault(keyAlgorithm, DEFAULT_KEY_ALGO);
        try {
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = null;
            if (Strings.isBlank(providerName)) {
                keyFactory = KeyFactory.getInstance(algorithm);
            } else {
                keyFactory = KeyFactory.getInstance(algorithm, providerName);
            }
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("load publicKey fail. " + e.getMessage());
        }
    }

    /**
     * 加载公钥（Base64）
     *
     * @param publicKeyBase64 秘钥为BASE64编码
     * @param keyAlgorithm    如：RSA
     * @param providerName    可以为空，如：BC
     * @return 公钥对象
     */
    public static PublicKey loadPublicKey(String publicKeyBase64, String keyAlgorithm, String providerName) {
        byte[] publicKeyBytes = Encodes.decodeBase64(publicKeyBase64);
        return loadPublicKey(publicKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * 加载公钥（Base64文件）
     *
     * @param publicKeyFile 公钥秘钥文件，内容为BASE64编码
     * @param keyAlgorithm  如：RSA
     * @param providerName  可以为空，如：BC
     * @return 公钥对象
     */
    public static PublicKey loadPublicKey(
            File publicKeyFile, String keyAlgorithm, String providerName) {
        byte[] publicKeyBytes = null;
        try {
            publicKeyBytes = Encodes.decodeBase64(FileUtils.readFileToString(publicKeyFile, "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        }
        return loadPublicKey(publicKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * 加载公钥（Base64文件,默认算法）
     *
     * @param publicKeyFile 公钥秘钥文件，内容为BASE64编码
     * @return 公钥对象
     */
    public static PublicKey loadPublicKey(File publicKeyFile) {
        return loadPublicKey(publicKeyFile, null, null);
    }

    /**
     * 加载公钥（x509证书）
     *
     * @param springResourceUri 证书文件URI
     * @return 公钥对象
     */
    public static PublicKey loadPublicKeyFromCert(String springResourceUri) {
        InputStream in = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(springResourceUri);
            in = resource.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 加载公钥(证书pem数据）
     *
     * @param pemCert pem证书数据
     * @return 公钥对象
     */
    public static PublicKey loadPublicKeyFromCertWithPem(String pemCert) {
        InputStream in = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            in = new ByteArrayInputStream(Encodes.decodeBase64(pemCert));
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 加载公钥(keystore)
     *
     * @param keystoreUri      秘钥库资源路径，采用spring资源管理模式，支持classpath:,file:和http等
     * @param keystoreType     秘钥库类型，可选JPKCS12（默认）或JKS
     * @param keystorePassword 秘钥库密码
     * @return 公钥对象
     */
    public static PublicKey loadPublicKeyFromKeyStore(
            String keystoreUri, String keystoreType, String keystorePassword) {
        InputStream in = null;
        try {
            KeyStore keyStore = loadKeystore(keystoreUri, keystoreType, keystorePassword);
            Enumeration<String> enumeration = keyStore.aliases();
            String keyAlias = null;
            if (enumeration.hasMoreElements()) {
                keyAlias = enumeration.nextElement();
            }
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载证书公钥失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 生成非对称秘钥对
     * 算法：RSA,长度：1024
     *
     * @return 秘钥对，Map<PublicKey,PrivateKey>
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGO);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("publicKey", publicKey);
        keyMap.put("privateKey", privateKey);
        return keyMap;
    }

    protected static byte[] getBytes(String data, String dataCharset) {
        String charset = Strings.isBlankDefault(dataCharset, DEFAULT_CHARSET);
        try {
            return data.getBytes(charset);
        } catch (Exception e) {
            throw new RuntimeException(data + "不支持编码:" + charset);
        }
    }

    /**
     * 加载KeyStore对象
     *
     * @param keystoreUri      秘钥库资源路径，采用spring资源管理模式，支持classpath:,file:和http等
     * @param keystoreType     秘钥库类型，可选JPKCS12（默认）或JKS
     * @param keystorePassword 秘钥库密码
     * @return KeyStore对象
     * @throws Exception
     */
    protected static KeyStore loadKeystore(String keystoreUri, String keystoreType, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        Resource resource = new DefaultResourceLoader().getResource(keystoreUri);
        try (InputStream in = resource.getInputStream()) {
            keyStore.load(in, keystorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载私钥失败:" + e.getMessage());
        }
        return keyStore;
    }
}
