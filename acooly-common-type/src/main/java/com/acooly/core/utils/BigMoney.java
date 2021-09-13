/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-05-26 18:17
 */
package com.acooly.core.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 可自定义精度的Money对象
 *
 * @author zhangpu
 * @date 2021-05-26 18:17
 */
public class BigMoney implements Serializable {

    public static final int DEFAULT_SCALE = 8;
    public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

    private BigDecimal value;
    private int scale = DEFAULT_SCALE;
    private int roundingMode = BigDecimal.ROUND_HALF_UP;


    public static BigMoney valueOf(BigDecimal value) {
        return new BigMoney(value);
    }

    public static BigMoney valueOf(String value) {
        return new BigMoney(value);
    }

    public static BigMoney ZERO() {
        return BigMoney.valueOf(BigDecimal.ZERO);
    }


    public BigMoney(BigDecimal value) {
        this(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(BigDecimal value, int scale) {
        this(value, scale, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(BigDecimal value, int scale, int roundingMode) {
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.value = value.setScale(this.scale, this.roundingMode);
    }

    public BigMoney(String value, int scale, int roundingMode) {
        this(new BigDecimal(value), scale, roundingMode);
    }

    public BigMoney(String value) {
        this(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(String value, int scale) {
        this(value, scale, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(double value, int scale, int roundingMode) {
        this(new BigDecimal(value), scale, roundingMode);
    }

    public BigMoney(double value) {
        this(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(double value, int scale) {
        this(value, scale, DEFAULT_ROUNDING_MODE);
    }


    public BigDecimal getAmount() {
        return this.value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getScale() {
        return scale;
    }

    public int getRoundingMode() {
        return roundingMode;
    }

    @Override
    public String toString() {
        return this.value.toPlainString();
    }


    public String toStandardString() {
        StringBuilder sb = new StringBuilder();
        BigDecimal bigDecimal = this.getAmount();
        long integerPart = bigDecimal.longValue();
        double decimalPart = bigDecimal.subtract(BigDecimal.valueOf(integerPart)).doubleValue();
        DecimalFormat fmt = new DecimalFormat(",###");
        sb.append(fmt.format(integerPart));
        if (decimalPart > 0) {
            String decimalString = String.valueOf(decimalPart);
            decimalString = decimalString.substring(1, decimalString.length());
            sb.append(decimalString);
        }
        return sb.toString();
    }


    public BigMoney add(BigMoney other) {
        return new BigMoney(this.value.add(other.value), this.scale, this.roundingMode);
    }


    public BigMoney addBy(BigMoney other) {
        this.value = this.value.add(other.value);
        return this;
    }

    public BigMoney subtract(BigMoney other) {
        return new BigMoney(this.value.subtract(other.value), this.scale, this.roundingMode);
    }

    public BigMoney subtractBy(BigMoney other) {
        this.value = this.value.subtract(other.value);
        return this;
    }

    public BigMoney multiply(BigMoney other) {
        return new BigMoney(this.value.multiply(other.value), this.scale, this.roundingMode);
    }

    public BigMoney multiplyBy(BigMoney other) {
        this.value = this.value.multiply(other.value);
        return this;
    }

    public BigMoney division(BigMoney other) {
        return new BigMoney(this.value.divide(other.value, this.scale, this.roundingMode), this.scale, this.roundingMode);
    }

    public BigMoney divisionBy(BigMoney other) {
        this.value = this.value.divide(other.value, this.scale, this.roundingMode);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BigMoney)) {
            return false;
        }

        BigMoney bigMoney = (BigMoney) o;

        if (!getValue().equals(bigMoney.getValue())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
