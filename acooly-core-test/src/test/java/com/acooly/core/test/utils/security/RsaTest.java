/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2023-06-11 19:31
 */
package com.acooly.core.test.utils.security;

import com.acooly.core.utils.security.RSA;
import com.acooly.core.utils.security.RSACipherCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangpu
 * @date 2023-06-11 19:31
 */
@Slf4j
public class RsaTest {

    @Test
    public void testMain() throws Exception {
        Map<String, Object> keys = RSA.genKeyPair();
        final PublicKey publicKey = (PublicKey) keys.get("publicKey");
        final PrivateKey privateKey = (PrivateKey) keys.get("privateKey");
        // 明文数据
        final String data = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // 签名和验签
        String sign = RSA.signBase64(data, privateKey);
        boolean verify = RSA.verifyBase64(data, sign, publicKey);
        System.out.println("plain:" + data);
        System.out.println("sign: " + sign);
        System.out.println("verify: " + verify);

        // 公钥加密和私钥解密
        int threadCount = 2;
        int testCount = 10;
        final int timesPerThread = 1;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(testCount);
        long start = System.currentTimeMillis();
        for (int i = 0; i < testCount; i++) {
            pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            // 线程内加密10次
                            String encrypt = null;
                            for (int j = 0; j < timesPerThread; j++) {
                                encrypt = RSA.encryptByPublicKeyBase64(data, publicKey, null);
                            }
                            // 线程内解密10次
                            String decrypt = null;
                            for (int j = 0; j < timesPerThread; j++) {
                                decrypt = RSA.decryptByPrivateKeyBase64(encrypt,
                                        privateKey, null);
                            }
                            System.out.println("thread" +
                                    Thread.currentThread().getName() + " encrypted:" +
                                    encrypt);
                            System.out.println("thread" +
                                    Thread.currentThread().getName() + " decrypted: " +
                                    decrypt);
                            System.out.println("thread" + Thread.currentThread().getId() + " cache stats:" + RSACipherCache.getStats());
                            countDownLatch.countDown();
                        }
                    });
        }
        countDownLatch.await();
        pool.shutdown();
        System.out.println("seconds: " + (System.currentTimeMillis() - start));
        System.out.println("count: " + testCount * timesPerThread);
    }
}
