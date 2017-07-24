package com.acooly.module.defence.url;

import java.lang.annotation.*;
/** @author qiubo@yiji.com */
@Target({ ElementType.PARAMETER,ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityParam {
    /**
     * 加密的参数的属性名，用于复杂对象
     */
   String[] value() default {};
}
