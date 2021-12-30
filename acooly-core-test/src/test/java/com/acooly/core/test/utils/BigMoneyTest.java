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
import java.math.MathContext;

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

    @Test
    public void testCent() {
        BigMoney.DEFAULT_SCALE = 4;
        BigMoney.DEFAULT_DB_MODE = 2;
        BigMoney b1 = new BigMoney("100000.1234");
        log.info("b1: {}", b1);
        log.info("b1 db_mode: {}", b1.getDbMode());

        BigDecimal val = b1.getValue();
        log.info("b1 scale: {}", val.scale());

        double d = Math.pow(10, val.scale());
        BigDecimal newVal = val.multiply(new BigDecimal(d));
        log.info("newVal: {}", newVal);
        log.info("newVal integer: {}", newVal.longValue());



    }


}
