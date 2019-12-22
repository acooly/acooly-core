/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-24 00:32
 */
package com.acooly.core.test.defence;

import com.acooly.module.defence.password.PasswordStrength;
import com.acooly.module.defence.password.Passwords;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-11-24 00:32
 */
@Slf4j
public class PasswordsTest {

    @Test
    public void testGenerate() {
        log.info("generate PasswordStrength.simple 12: {}", Passwords.generate(PasswordStrength.simple, 12));
        log.info("generate PasswordStrength.usually 12: {}", Passwords.generate(PasswordStrength.usually, 12));
        log.info("generate PasswordStrength.complex 12: {}", Passwords.generate(PasswordStrength.complex, 12));
        log.info("generate PasswordStrength.complex 12 batch: 20");
        List<String> passwords = Passwords.generates(PasswordStrength.complex, 12, 10);
        for (String password : passwords) {
            log.info(password);
        }
    }


}
