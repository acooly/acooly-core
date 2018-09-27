package com.acooly.module.mybatis.interceptor;

import com.acooly.core.common.dao.support.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;

import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-09-27 22:01
 */
@Slf4j
public abstract class AbstractInterceptor {
    public PageInfo havePageInfoArg(Invocation invocation) {
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return null;
        }
        if (Map.class.isAssignableFrom(parameter.getClass())) {
            for (Object v : ((Map) parameter).values()) {
                if (v instanceof PageInfo) {
                    return (PageInfo) v;
                }
            }
        } else if (parameter instanceof PageInfo) {
            return (PageInfo) parameter;
        }
        return null;
    }
    public MappedStatement createNewMappedStatement(MappedStatement mappedStatement, BoundSql boundSql, String newSql) {
        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, newSql);
        MappedStatement newMapperStmt = copyFromMappedStatement(mappedStatement, newBoundSql);
        return newMapperStmt;
    }
    public BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql =
                new BoundSql(
                        ms.getConfiguration(),
                        sql,
                        boundSql.getParameterMappings(),
                        boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    public MappedStatement copyFromMappedStatement(MappedStatement ms, final BoundSql newBoundSql) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(
                        ms.getConfiguration(),
                        ms.getId(),
                        parameterObject -> newBoundSql,
                        ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
}
