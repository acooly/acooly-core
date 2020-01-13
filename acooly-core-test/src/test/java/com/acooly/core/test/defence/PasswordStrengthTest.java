/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 19:36
 */
package com.acooly.core.test.defence;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Regexs;
import com.acooly.module.defence.exception.DefenceErrorCodes;
import com.acooly.module.defence.password.PasswordStrength;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangpu
 * @date 2019-11-23 19:36
 */
@Slf4j
public class PasswordStrengthTest {



    @Test
    public void testPasswordStrength() {

        log.info("test simple:");
        doTestPasswordStrength("1234567890", PasswordStrength.simple);
        doTestPasswordStrength("Zp1234567890!#$%", PasswordStrength.simple);
        doTestPasswordStrength("1234567890", PasswordStrength.usually);
        doTestPasswordStrength("1234567890", PasswordStrength.complex);
        doTestPasswordStrength("12345678901234567", PasswordStrength.simple);

        log.info("test usually:");
        doTestPasswordStrength("Ab123456", PasswordStrength.usually);
        doTestPasswordStrength("Ab123456%$#", PasswordStrength.usually);
        doTestPasswordStrength("Ab12345678901234", PasswordStrength.usually);
        doTestPasswordStrength("Ab123456789012345", PasswordStrength.usually);
        doTestPasswordStrength("12345612", PasswordStrength.usually);
        doTestPasswordStrength("ab123456", PasswordStrength.usually);
        doTestPasswordStrength("1Ab123456", PasswordStrength.usually);
        doTestPasswordStrength("#Ab123456", PasswordStrength.usually);
        doTestPasswordStrength("Ab1234", PasswordStrength.usually);

        log.info("test complex:");
        doTestPasswordStrength("1234567890", PasswordStrength.complex);
        doTestPasswordStrength("Ab123456%$#", PasswordStrength.complex);
        doTestPasswordStrength("9!%wfzw(xNaJ", PasswordStrength.complex);
        doTestPasswordStrength("tdo$qTi8*(Xh", PasswordStrength.complex);
        doTestPasswordStrength("vt(NT78DE1K3", PasswordStrength.complex);
        doTestPasswordStrength("D#s3u65hmwKu", PasswordStrength.complex);
        doTestPasswordStrength("Ab12345678901234", PasswordStrength.complex);
        doTestPasswordStrength("Ab123456789012345", PasswordStrength.complex);
        doTestPasswordStrength("ab123456", PasswordStrength.complex);
        doTestPasswordStrength("Ab123456", PasswordStrength.complex);
        doTestPasswordStrength("1Ab123456", PasswordStrength.complex);
        doTestPasswordStrength("Ab1234", PasswordStrength.complex);
    }


    protected void doTestPasswordStrength(String password, PasswordStrength passwordStrength) {
        try {
            passwordStrength.verify(password);
            log.info("PasswordStrength:[{}] test ok。password: {}", passwordStrength.code(), password);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(BusinessException.class);
            BusinessException be = (BusinessException) e;
            assertThat(be.getCode()).isEqualTo(DefenceErrorCodes.PASSWORD_STRENGTH_VERIFY_ERROR.code());
            log.info("PasswordStrength:[{}] test no。password: {}, error: {}", passwordStrength.code(), password,be.getMessage());
        }
    }

}
