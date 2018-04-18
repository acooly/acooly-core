package com.acooly.module.ds.check.dic;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 列元数据
 */
@Setter
@Getter
public class Column {

    /**
     * 字符类型
     */
    public static final int DATATYPE_STRING = 0;
    /**
     * 数字类型
     */
    public static final int DATATYPE_LONG = 1;
    /**
     * 日期时间类型
     */
    public static final int DATATYPE_DATE = 2;
    /**
     * 对象类型
     */
    public static final int DATATYPE_LOB = 3;

    public static final int DATATYPE_INT = 4;

    /**
     * 精确小数
     */
    public static final int BIG_DECIMAL = 7;

    /**
     * 枚举类型
     */
    public static final int DATATYPE_ENUM = 10;

    /**
     * 列名称
     */
    private String name;
    /**
     * 列类型
     */
    private String dataType;
    /**
     * 列长度
     */
    private int length;
    /**
     * 列小数长度
     */
    private int scale = 0;
    /**
     * 是否可以为空
     */
    private boolean nullable;
    /**
     * 默认值
     */
    private Object defaultValue;
    /**
     * 列备注
     */
    private String common;
    /**
     * 常用数字分类字段可选值
     */
    private Map<String, String> options;

    private String validations;
    /**
     * 表额外信息
     */
    private String extra;
}
