/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-07 20:49
 */
package com.acooly.core.utils.ie.anno;

import java.lang.annotation.*;

/**
 * 导出格式
 * <p>
 * 目前用于Excel的Cell
 *
 * @author zhangpu
 * @date 2022-08-07 20:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ExportStyle {

    /**
     * [可选] 字体名称 例如：微软雅黑
     *
     * @return 字体名称
     */
    String fontName() default "";

    /**
     * [可选] 字体大小 例如：11（默认）
     *
     * @return 字体大小
     */
    short fontSize() default -1;

    /**
     * [可选] 粗体 例如：true表示粗体，默认false
     *
     * @return true表示粗体，默认false
     */
    boolean fontBold() default false;

    /**
     * [可选] 背景填充颜色值（16进制格式） 例如：#DDEBF7，默认无
     *
     * @return 背景填充颜色值
     */
    String backgroundColor() default "";

    /**
     * [可选] 行高 例如：400（与默认值差不多）
     *
     * @return 行高
     */
    short rowHeight() default -1;
}
