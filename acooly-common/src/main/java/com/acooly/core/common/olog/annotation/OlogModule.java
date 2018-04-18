package com.acooly.core.common.olog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OlogModule {

    /**
     * 模块
     */
    String module() default "";

    /**
     * 模块名称
     */
    String moduleName() default "";

}
