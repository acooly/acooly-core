package com.acooly.module.mybatis.interceptor;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.dao.support.SearchFilter;
import com.acooly.core.utils.Exceptions;
import com.acooly.core.utils.Strings;
import com.acooly.module.mybatis.SearchFilterParser;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

/**
 * 关联查询生成where语句
 *
 * @author qiuboboy@qq.com
 * @date 2018-09-27 16:23
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@SuppressWarnings({"unchecked","rawtypes"})
public class ExInterceptor extends AbstractInterceptor implements Interceptor {
	private Map<String, Class> domanClassCache = Maps.newConcurrentMap();
    private Map<String, Class> fieldTypeCache = Maps.newConcurrentMap();

	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        PageInfo pageInfo = havePageInfoArg(invocation);
        if (pageInfo == null) {
            return invocation.proceed();
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        MapperMethod.ParamMap parameterObject = (MapperMethod.ParamMap) boundSql.getParameterObject();
        if (parameterObject.containsKey("com.acooly.module.mybatis.ex.ListMapper.ListSqlSource")) {
            return invocation.proceed();
        }
        // 待参数的原始SQL
        String originalSql = boundSql.getSql().trim();
        if ( originalSql.toLowerCase().contains(" where ")) {
            return invocation.proceed();
        }

        Map<String, Object> map = (Map<String, Object>) parameterObject.get("param2");
        if (map == null || map.isEmpty()) {
            return invocation.proceed();
        }
        Map<String, Boolean> sortMap = (Map<String, Boolean>) parameterObject.get("param3");
        String id = mappedStatement.getId();
        String daoClassName = Strings.substringBeforeLast(id, ".");
        StringBuilder sqlResult = new StringBuilder();
        sqlResult.append(originalSql);
        if (map != null && !map.isEmpty()) {
            sqlResult.append(" WHERE ");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                SearchFilter searchFilter = SearchFilter.parse(entry.getKey(), entry.getValue());
                if (searchFilter == null) {
                    continue;
                }
                sqlResult.append(SearchFilterParser.parseSqlField(searchFilter, getFieldType(daoClassName, searchFilter.fieldName)));
                sqlResult.append(" AND ");
            }
            sqlResult.delete(sqlResult.length() - 4, sqlResult.length() - 1);
        }
        sqlResult.append("order by ");
        if (sortMap == null || sortMap.isEmpty()) {
            sqlResult.append(" id desc");
        } else {
            for (Map.Entry<String, Boolean> entry : sortMap.entrySet()) {
                sqlResult.append(
                        Strings.camelToUnderline(entry.getKey())
                                + "  "
                                + (entry.getValue() ? "ASC" : "DESC"));
                sqlResult.append(",");
            }
            sqlResult.deleteCharAt(sqlResult.length() - 1);
        }
        invocation.getArgs()[0] = createNewMappedStatement(mappedStatement, boundSql, sqlResult.toString());
        return invocation.proceed();
    }


    private Class getFieldType(String daoClassName, String fieldName) {
        String key = daoClassName + "#" + fieldName;
        Class fieldType = fieldTypeCache.get(key);
        if (fieldType == null) {
            Class domainType = getDomainType(daoClassName);
            BeanWrapper beanWrapper = new BeanWrapperImpl(domainType);
            beanWrapper.setAutoGrowNestedPaths(true);
            PropertyDescriptor propertyDescriptor = beanWrapper.getPropertyDescriptor(fieldName);
            fieldType = propertyDescriptor.getPropertyType();
            fieldTypeCache.put(key, fieldType);
        }
        return fieldType;
    }


    private Class getDomainType(String daoClassName) {
        Class domanClass = domanClassCache.get(daoClassName);
        if (domanClass != null) {
            return domanClass;
        } else {
            ParameterizedType parameterizedType = null;
            try {
                parameterizedType = (ParameterizedType) Class.forName(daoClassName).getGenericInterfaces()[0];
                Type[] params = ((ParameterizedType) parameterizedType).getActualTypeArguments();
                domanClass = (Class) params[0];
                domanClassCache.put(daoClassName, domanClass);
                return domanClass;
            } catch (ClassNotFoundException e) {
                log.error("", e);
                throw Exceptions.rethrowBusinessException(e);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
