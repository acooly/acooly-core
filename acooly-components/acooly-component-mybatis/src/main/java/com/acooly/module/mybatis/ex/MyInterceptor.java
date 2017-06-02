/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 20:16 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Properties;

/** @author qiubo@yiji.com */
@Intercepts({
  @Signature(
    method = "query",
    type = Executor.class,
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
  ),
  @Signature(
    method = "prepare",
    type = StatementHandler.class,
    args = {Connection.class}
  )
})
public class MyInterceptor implements Interceptor {

  public Object intercept(Invocation invocation) throws Throwable {
    Object result = invocation.proceed();
    System.out.println("Invocation.proceed()");
    return result;
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties) {
    String prop1 = properties.getProperty("prop1");
    String prop2 = properties.getProperty("prop2");
    System.out.println(prop1 + "------" + prop2);
  }
}
