/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-05-26 18:17
 */
package com.acooly.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangpu
 * @date 2021-05-26 18:17
 */
public class BigMoney {

    public static final int DEFAULT_SCALE = 8;
    public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    private BigDecimal value;
    private int scale = DEFAULT_SCALE;
    private int roundingMode = BigDecimal.ROUND_HALF_UP;

    public BigMoney(BigDecimal value) {
        this(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(BigDecimal value, int scale) {
        this(value, scale, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(BigDecimal value, int scale, int roundingMode) {
        this.value = value;
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.value.setScale(this.scale, this.roundingMode);
    }

    public BigMoney add(BigMoney other) {
        return new BigMoney(this.value.add(other.value), this.scale, this.roundingMode);
    }

    public BigMoney subtract(BigMoney other) {
        return null;
    }

    public BigMoney multiply(BigMoney other) {
        return null;
    }

    public BigMoney division(BigMoney other) {
        return null;
    }



    public static void main(String[] args) {
//        BigMoney c100 = new BigMoney(new BigDecimal(100));

    }


}
