package com.acooly.core.common.olog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Olog {

    /**
     * 系统
     */
    String system() default "";

    /**
     * 模块
     */
    String module() default "";

    /**
     * 模块名称
     */
    String moduleName() default "";

    /**
     * 操作名称
     */
    String action() default "";

    String actionName() default "";

    /**
     * 忽略日志收集
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ignore {
    }
}
