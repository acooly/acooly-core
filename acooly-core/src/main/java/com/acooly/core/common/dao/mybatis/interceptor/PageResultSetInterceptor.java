package com.acooly.core.common.dao.mybatis.interceptor;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.acooly.core.common.dao.support.PageInfo;

@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PageResultSetInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		try {
			DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
			MetaObject metaResultSetHandler = SystemMetaObject.forObject(resultSetHandler);
			ParameterHandler parameterHandler = (ParameterHandler) metaResultSetHandler.getValue("parameterHandler");
			Object parameterObject = parameterHandler.getParameterObject();
			PageInfo pageInfo = null;
			if (parameterObject instanceof MapperMethod.ParamMap) {
				MapperMethod.ParamMap paramMapObject = (MapperMethod.ParamMap) parameterObject;
				if (paramMapObject != null) {
					for (Object key : paramMapObject.keySet()) {
						if (paramMapObject.get(key) instanceof PageInfo) {
							pageInfo = (PageInfo) paramMapObject.get(key);
							break;
						}
					}
				}
			}
			// 请求参数中有pageInfo参数
			if (pageInfo != null) {
				List pageResults = (List) invocation.proceed();
				pageInfo.setPageResults(pageResults);
				return pageInfo;
			}
		} catch (Exception e) {
			throw new Exception("handle ResultSetHandler Fail.", e);
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
