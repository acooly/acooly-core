package com.acooly.module.mybatis.interceptor;

import com.acooly.core.common.dao.dialect.DatabaseDialectManager;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.mybatis.metadata.CountSql;
import com.acooly.module.mybatis.page.MyBatisPage;
import com.github.pagehelper.parser.CountSqlParser;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
@Order(0)
public class PageExecutorInterceptor implements Interceptor {

    private static Map<String, String> countSqlMap = Maps.newConcurrentMap();

    CountSqlParser countSqlParser = new CountSqlParser();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PageInfo pageInfo = havePageInfoArg(invocation);
        if (pageInfo == null) {
            return invocation.proceed();
        }

        // 开始处理分页拦截
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 待参数的原始SQL
        String originalSql = boundSql.getSql().trim();
        Object parameterObject = boundSql.getParameterObject();

        // 总记录数
        long totalCount = getCount(mappedStatement, originalSql, boundSql, parameterObject);
        long totalPage =
                (totalCount % pageInfo.getCountOfCurrentPage() == 0
                        ? totalCount / pageInfo.getCountOfCurrentPage()
                        : totalCount / pageInfo.getCountOfCurrentPage() + 1);
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalPage);

        // 构建新的分页查询SQL
        String pageSql = getPageSql(mappedStatement, pageInfo, originalSql);
        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, pageSql);
        MappedStatement newMapperStmt = copyFromMappedStatement(mappedStatement, newBoundSql);
        invocation.getArgs()[0] = newMapperStmt;
        List result = (List) invocation.proceed();
        if (result == null) {
            pageInfo.setPageResults(Collections.emptyList());
        } else {
            pageInfo.setPageResults(result);
        }
        MyBatisPage page = new MyBatisPage<>(result, totalCount, totalPage);
        page.setCurrentPage(pageInfo.getCurrentPage());
        page.setCountOfCurrentPage(pageInfo.getCountOfCurrentPage());
        return page;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {
    }

    @SuppressWarnings("rawtypes")
    private String getPageSql(MappedStatement mappedStatement, PageInfo pageInfo, String originalSql)
            throws Throwable {
        Connection connection =
                mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        return DatabaseDialectManager.pageSql(connection, pageInfo, originalSql);
    }

    private long getCount(
            MappedStatement mappedStatement,
            String originalSql,
            BoundSql boundSql,
            Object parameterObject)
            throws Throwable {
        String countSql = getCountSql(mappedStatement, originalSql);
        Connection connection = null;
        long totpage = 0;
        try {
            connection =
                    mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            PreparedStatement countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
            DefaultParameterHandler parameterHandler =
                    new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
            parameterHandler.setParameters(countStmt);
            ResultSet rs = countStmt.executeQuery();
            totpage = 0;
            if (rs.next()) {
                totpage = rs.getLong(1);
            }
            rs.close();
            countStmt.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return totpage;
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     */
    private String getCountSql(MappedStatement mappedStatement, String sql) {
        String countSqlByAnnotation = getCountSqlByAnnotation(mappedStatement);
        return !Strings.isNullOrEmpty(countSqlByAnnotation)
                ? countSqlByAnnotation
                : countSqlParser.getSmartCountSql(sql);
    }

    private String getCountSqlByAnnotation(MappedStatement mappedStatement) {
        String id = mappedStatement.getId();
        String cached = countSqlMap.get(id);
        if (cached == null) {
            String countSql = "";
            try {
                if (!Strings.isNullOrEmpty(id)) {
                    int idx = id.lastIndexOf('.');
                    String className = id.substring(0, idx);
                    String methodName = id.substring(idx + 1);
                    Method[] methods;
                    methods = Class.forName(className).getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals(methodName)) {
                            CountSql annotation = method.getAnnotation(CountSql.class);
                            if (annotation != null) {
                                countSql = annotation.value();
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //ignore
            }
            countSqlMap.put(id, countSql);
            cached = countSql;
        }
        return cached;
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, final BoundSql newBoundSql) {
        Builder builder =
                new Builder(
                        ms.getConfiguration(),
                        ms.getId(),
                        parameterObject -> newBoundSql,
                        ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        // builder.keyProperty(ms.getKeyProperties())
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
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

    private PageInfo havePageInfoArg(Invocation invocation) {
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
}
