package com.acooly.module.mybatis.metadata;

import java.lang.annotation.*;

/**
 * 标注于EntityMybatisDao接口方法上，当分页查询时，使用此sql来查询数据条数
 *
 * @author qiubo@yiji.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CountSql {
    String value();
}
