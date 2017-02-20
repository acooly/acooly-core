/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-15 20:47 创建
 */
package com.acooly.module.mybatis.interceptor;

import com.acooly.core.common.domain.AbstractEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.util.Date;
import java.util.Properties;

/**
 * @author qiubo@yiji.com
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class DateInterceptor implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		if (args != null) {
            //删除时不改变值
            if(args.length==2){
                if(args[0] instanceof MappedStatement){
                    MappedStatement statement= (MappedStatement) args[0];
                    if(statement.getSqlCommandType()== SqlCommandType.DELETE){
                        return invocation.proceed();
                    }
                }
            }
            //更新改变值
            for (Object arg : args) {
                if(arg instanceof AbstractEntity){
					AbstractEntity abstractEntity=(AbstractEntity)arg;
					if(abstractEntity.isNew()){
						abstractEntity.setCreateTime(new Date());
					}else {
						abstractEntity.setUpdateTime(new Date());
					}
                }
            }
        }
        return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	@Override
	public void setProperties(Properties properties) {
		
	}
}
