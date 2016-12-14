package com.acooly.core.common.dao.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记MyBatis的Dao，用于扫描
 * 
 * @author zhangpu
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBatisMapper {

}
