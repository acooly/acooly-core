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
     * 系统名称
     */
    String sysName();

    /**
     * http端口[-1=关闭,0=随机]
     */
    int httpPort() default 0;
}
