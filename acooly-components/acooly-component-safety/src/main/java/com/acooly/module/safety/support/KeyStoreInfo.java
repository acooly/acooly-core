/**
 * create by zhangpu
 * date:2015年3月11日
 */
package com.acooly.module.safety.support;

import com.acooly.core.utils.security.RSA;
import com.acooly.module.safety.exception.SafetyException;
import com.acooly.module.safety.exception.SafetyResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * @author zhangpu
 */
@Slf4j
public class KeyStoreInfo extends KeySupport {

    public static final String KEY_STORE_JKS = "JKS";
    public static final String KEY_STORE_PKCS12 = "PKCS12";

    private String keyStoreType = KEY_STORE_PKCS12;

    /**
     * keyStore路径，支持classpath:配置
     */
    private String keyStoreUri;

    /**
     * keyStore密码
     */
    private String keyStorePassword;

    /**
     * 证书路径，支持classpath:配置
     */
    private String certificateUri;

    /**
     * 签名结果字符串编码
     */
    private CodecEnum signatureCodec = CodecEnum.BASE64;

    private String signatureAlgo = RSA.SIGN_ALGO_SHA1;

    /**
     * 签名明文字符串编码
     */
    private String plainEncode = "UTF-8";


    private PrivateKey privateKey;

    private X509Certificate certificate;

    public KeyStoreInfo loadKeys() {
        if (privateKey == null) {
            synchronized (this) {
                if (privateKey == null) {
                    loadPrivateKey();
                }
            }
        }
        if (certificate == null) {
            synchronized (this) {
                if (certificate == null) {
                    loadCertificate();
                }
            }
        }
        return this;
    }


    protected void initKey() {
        loadPrivateKey();
        loadCertificate();
    }

    private void loadPrivateKey() {
        InputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(getKeyStoreType());
            Resource resource = new DefaultResourceLoader().getResource(getKeyStoreUri());
            in = resource.getInputStream();
            keyStore.load(in, getKeyStorePassword().toCharArray());

            Enumeration<String> enumas = keyStore.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, getKeyStorePassword().toCharArray());
            this.privateKey = privateKey;
            log.debug("加载keystore私钥 - [成功] , privateKey:{}", this.privateKey);
        } catch (Exception e) {
            log.warn("加载keystore私钥 - [失败] , 原因: {}", e.getMessage());
            throw new SafetyException(SafetyResultCode.LOAD_KEYSTORE_PRIVATE_ERROR, e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private void loadCertificate() {
        InputStream in = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(getCertificateUri());
            in = resource.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(in);
            this.certificate = x509Certificate;
            log.debug("加载证书 - [成功] , cert:{}", x509Certificate);
        } catch (Exception e) {
            log.warn("加载证书 - [失败] , 原因: {}", e.getMessage());
            throw new SafetyException(SafetyResultCode.LOAD_CERTIFICATE_ERROR, e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }


    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStoreUri() {
        return keyStoreUri;
    }

    public void setKeyStoreUri(String keyStoreUri) {
        this.keyStoreUri = keyStoreUri;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getCertificateUri() {
        return certificateUri;
    }

    public void setCertificateUri(String certificateUri) {
        this.certificateUri = certificateUri;
    }

    public CodecEnum getSignatureCodec() {
        return signatureCodec;
    }

    public void setSignatureCodec(CodecEnum signatureCodec) {
        this.signatureCodec = signatureCodec;
    }

    public String getSignatureAlgo() {
        return signatureAlgo;
    }

    public void setSignatureAlgo(String signatureAlgo) {
        this.signatureAlgo = signatureAlgo;
    }

    public String getPlainEncode() {
        return plainEncode;
    }

    public void setPlainEncode(String plainEncode) {
        this.plainEncode = plainEncode;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }
}
