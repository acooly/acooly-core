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

    /**
     * 默认精度：小数点后8位
     */
    public static int DEFAULT_SCALE = 8;
    /**
     * 默认取舍模式：四舍五入
     */
    public static int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    /**
     * 数据库存储模式。 1:存Decimal(BigDecimal), 2:存BIGINT(Long)
     */
    public static final int DB_MODE_BIG_DECIMAL = 1;
    public static final int DB_MODE_LONG = 2;
    public static int DEFAULT_DB_MODE = DB_MODE_BIG_DECIMAL;

    private BigDecimal value;
    private int scale = DEFAULT_SCALE;
    private int roundingMode = BigDecimal.ROUND_HALF_UP;
    private int dbMode = DEFAULT_DB_MODE;


    public static BigMoney valueOf(BigDecimal value) {
        return new BigMoney(value);
    }

    public static BigMoney centOf(Long cent) {
        double d = Math.pow(10, BigMoney.DEFAULT_SCALE);
        return BigMoney.valueOf(new BigDecimal(cent).divide(new BigDecimal(d)));
    }

    public static BigMoney valueOf(String value) {
        return new BigMoney(value);
    }

    public static BigMoney ZERO() {
        return BigMoney.valueOf(BigDecimal.ZERO);
    }

    public BigMoney(BigDecimal value, int scale, int roundingMode, int dbMode) {
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.value = value.setScale(this.scale, this.roundingMode);
        this.dbMode = dbMode;
    }

    public BigMoney(BigDecimal value, int scale, int roundingMode) {
        this(value, scale, roundingMode, DEFAULT_DB_MODE);
    }

    public BigMoney(BigDecimal value) {
        this(value, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigMoney(BigDecimal value, int scale) {
        this(value, scale, DEFAULT_ROUNDING_MODE);
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
        String toString = this.toString();
        while (toString.endsWith("0")) {
            toString = toString.substring(0, toString.length() - 1);
        }
        if (toString.endsWith(".")) {
            toString = toString.substring(0, toString.length() - 1);
        }
        return toString;
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

    /**
     * 获取"分"整数值。
     * 注意：该值与精度相关。例如：进度为8，则该值为：`value * 10^8`然后取整数
     *
     * @return 分，整数值
     */
    public Long getCent() {
        double d = Math.pow(10, this.value.scale());
        return value.multiply(new BigDecimal(d)).longValue();
    }

    public int getDbMode() {
        return dbMode;
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
