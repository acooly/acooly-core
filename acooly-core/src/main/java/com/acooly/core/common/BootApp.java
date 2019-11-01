package com.acooly.core.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.*;

/**
 * @author qiubo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
public @interface BootApp {
    /**
     * 系统名称(唯一)
     */
    String sysName();


    /**
     * 系统中文名称
     *
     * @return
     */
    String sysTitle() default "";


    /**
     * 归属（作者）
     *
     * @return
     */
    String owner() default "";


    /**
     * http端口[-1=关闭,0=随机]
     */
    int httpPort() default 0;
}
