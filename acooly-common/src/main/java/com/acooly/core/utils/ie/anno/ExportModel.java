/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 09:22
 */
package com.acooly.core.utils.ie.anno;

import java.lang.annotation.*;

/**
 * @author zhangpu
 * @date 2022-08-03 09:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ExportModel {

    /**
     * [可选] 文件名或sheet名称
     * 为空时，采用自动代码生成(实体的备注名)或前端界面指定的文件名。
     *
     * @return 文件名
     */
    String name() default "";

    /**
     * 是否显示表头
     *
     * @return
     */
    boolean headerShow() default true;

    /**
     * 表头样式
     *
     * @return
     */
    ExportStyle headerStyle() default @ExportStyle;


    /**
     * Excel时有效，表格是否有边框
     *
     * @return
     */
    boolean border() default false;


    /**
     * [可选] 针对配置了@ExportColumn的列表的全局忽略配置
     * 一般用于对基类里面的属性的忽略，多个属性名间使用逗号分隔
     * 优先级最高，高于exportColumns
     *
     * @return sheeName
     */
    String[] ignores() default {};

    /**
     * [可选] 直接配置ExportColumn列表
     * <p>
     * 用于配置父类列或覆盖父类列配置导出参数
     * <p>
     * 该配置的优先级高于属性列上的配置
     *
     * @return
     */
    ExportColumn[] exportColumns() default {};

}
