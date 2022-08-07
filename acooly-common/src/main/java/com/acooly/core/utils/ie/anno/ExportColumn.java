/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 08:52
 */
package com.acooly.core.utils.ie.anno;

import java.lang.annotation.*;

/**
 * @author zhangpu
 * @date 2022-08-03 08:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ExportColumn {

    /**
     * 列标题
     *
     * @return 标题
     */
    String header() default "";

    /**
     * 属性名
     * 在@ExportModel内配置父类属性时使用
     *
     * @return
     */
    String name() default "";

    /**
     * 顺序，数值越大越靠后
     *
     * @return int
     */
    int order() default 0;

    /**
     * 宽度
     * <p>
     * Excel有效；如果未配置，则默认根据字符串长度计算，最大255个字符宽度（UTF-8）
     *
     * @return 宽度
     */
    int width() default -1;


    /**
     * 格式化，时间、金额等
     *
     * @return 格式化
     */
    String format() default "";


    /**
     * 枚举（Messagable）显示message还是name（code）
     *
     * @return
     */
    boolean showMapping() default true;
}
