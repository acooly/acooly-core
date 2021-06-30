/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-05-26 17:46
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.BigMoney;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author zhangpu
 * @date 2021-05-26 17:46
 */
@Slf4j
public class BigMoneyTest {

    @Test
    public void testCalc() {

        BigDecimal n100 = new BigDecimal(0.11);
//        n100.setScale(8);
        BigDecimal n3 = new BigDecimal(4);
//        n3.setScale(8);

        BigDecimal divideResult = n100.divide(n3, 8, BigDecimal.ROUND_HALF_UP);
        System.out.println(divideResult.toPlainString());

    }


    @Test
    public void testAdd() {
        BigMoney b1 = new BigMoney("100000.12345678");
        BigMoney b2 = new BigMoney(100000.00000002);
        BigMoney b3 = b1.add(b2);
        System.out.println(b3);
    }


}
