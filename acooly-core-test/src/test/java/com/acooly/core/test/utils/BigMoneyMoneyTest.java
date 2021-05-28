/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-05-26 17:46
 */
package com.acooly.core.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author zhangpu
 * @date 2021-05-26 17:46
 */
@Slf4j
public class BigMoneyMoneyTest {

    @Test
    public void testCalc() {

        BigDecimal n100 = new BigDecimal(0.11);
//        n100.setScale(8);
        BigDecimal n3 = new BigDecimal(4);
//        n3.setScale(8);

        BigDecimal divideResult = n100.divide(n3,8,BigDecimal.ROUND_HALF_UP);
        System.out.println(divideResult.toPlainString());

    }


}
