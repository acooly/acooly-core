/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 08:52
 */
package com.acooly.core.utils.ie;

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
    String title() default "";

    /**
     * 属性名
     * 在@ExportModel内配置父类属性时使用
     * @return
     */
    String name() default "";

    /**
     * 顺序，数值越大越靠后
     *
     * @return int
     */
    int order() default 0;

}
