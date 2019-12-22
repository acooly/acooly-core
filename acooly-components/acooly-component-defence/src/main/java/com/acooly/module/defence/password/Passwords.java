/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 23:43
 */
package com.acooly.module.defence.password;

import com.acooly.core.utils.Asserts;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * 安全密码工具
 *
 * @author zhangpu
 * @date 2019-11-23 23:43
 */
@Slf4j
public class Passwords {

    private final static String SPECIALS = "!@#$%^&*()_+-=";

    /**
     * 验证密码强度
     *
     * @param password
     * @param passwordStrength
     */
    public static void verify(String password, PasswordStrength passwordStrength) {
        passwordStrength.verify(password);
    }

    /**
     * 判断密码强度
     *
     * @param password
     * @return
     */
    public static PasswordStrength determine(String password) {
        return PasswordStrength.determine(password);
    }

    /**
     * 批量生产密码
     * 以待选择
     *
     * @param passwordStrength
     * @param count
     * @param batchSize
     * @return
     */
    public static List<String> generates(PasswordStrength passwordStrength, Integer count, int batchSize) {
        Asserts.between(batchSize, 1, 20, "批次大小", "批次大小最大不超过20");
        List<String> passwords = Lists.newArrayList();
        for (int i = 0; i < batchSize; i++) {
            passwords.add(generate(passwordStrength, count));
        }
        return passwords;
    }

    /**
     * 生成密码
     *
     * @param passwordStrength
     * @param count
     * @return
     */
    public static String generate(PasswordStrength passwordStrength, Integer count) {
        if (count == null) {
            count = 12;
        }
        Asserts.between(count, 8, 16, "密码", "密码长度必须在8-16字符间");

        if (passwordStrength == PasswordStrength.simple) {
            return RandomStringUtils.randomNumeric(count);
        }


        String usually = null;
        while (true) {
            usually = RandomStringUtils.randomAlphanumeric(count);
            if (PasswordStrength.determine(usually) == PasswordStrength.usually) {
                break;
            }
        }

        if (passwordStrength == PasswordStrength.usually) {
            return usually;
        }

        char[] chars = usually.toCharArray();
        String specs = RandomStringUtils.random(3, 0,
                SPECIALS.length(), false, false, SPECIALS.toCharArray());
        for (char spec : specs.toCharArray()) {
            while (true) {
                int index = RandomUtils.nextInt(1, chars.length - 2);
                if (chars[index] != spec) {
                    chars[index] = spec;
                    break;
                }
            }
        }
        return new String(chars);
    }


}
