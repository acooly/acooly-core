package com.acooly.module.defence.url;

import com.acooly.module.defence.DefenceProperties;
import com.google.common.base.Charsets;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qiubo@yiji.com
 */
@Service
public class UrlSecurityServiceImpl implements InitializingBean, UrlSecurityService {


    @Autowired
    private DefenceProperties defenceProperties;
    private AesCipherService cipherService;
    private byte[] key;

    public String encrypt(String text) {
        if (text == null) {
            return null;
        }
        return cipherService.encrypt(text.getBytes(Charsets.UTF_8), key).toHex();
    }

    public String decrypt(String encrypted) {
        if (encrypted == null) {
            return null;
        } else {
            byte[] bytes;
            try {
                bytes = cipherService.decrypt(Hex.decode(encrypted), key).getBytes();
            } catch (Exception e) {
                throw new IllegalArgumentException("密文不安全", e);
            }
            if (bytes.length == 0) {
                throw new IllegalArgumentException("密文不安全");
            }
            return new String(bytes, Charsets.UTF_8);
        }
    }

    public void setDefenceProperties(DefenceProperties defenceProperties) {
        this.defenceProperties = defenceProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cipherService = new AesCipherService();
        cipherService.setKeySize(128);
        key = defenceProperties.getUrl().paddingKey().getBytes(Charsets.UTF_8);
    }
}
