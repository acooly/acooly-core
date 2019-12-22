package com.acooly.module.mybatis.interceptor;

import com.acooly.core.common.dao.dialect.DatabaseDialectManager;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.mybatis.metadata.CountSql;
import com.acooly.module.mybatis.page.MyBatisPage;
import com.github.pagehelper.parser.CountSqlParser;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionHolder;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
@Order(0)
public class PageExecutorInterceptor extends AbstractInterceptor implements Interceptor {

    private static  Map<String, String> countSqlMap = Maps.newConcurrentMap();

    CountSqlParser countSqlParser = new CountSqlParser();

    private SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object intercept( Invocation invocation ) throws Throwable {
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
                ( totalCount % pageInfo.getCountOfCurrentPage() == 0
                        ? totalCount / pageInfo.getCountOfCurrentPage()
                        : totalCount / pageInfo.getCountOfCurrentPage() + 1 );
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalPage);

        // 构建新的分页查询SQL
        String pageSql = getPageSql(mappedStatement, pageInfo, originalSql);
        invocation.getArgs()[0] = createNewMappedStatement(mappedStatement, boundSql, pageSql);
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
    public Object plugin( Object arg0 ) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties( Properties arg0 ) {
    }

    @SuppressWarnings("rawtypes")
    private String getPageSql( MappedStatement mappedStatement, PageInfo pageInfo,
            String originalSql )
            throws Throwable {
        Connection connection =
                mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        return DatabaseDialectManager.pageSql(connection, pageInfo, originalSql);
    }

    private long getCount(
            MappedStatement mappedStatement,
            String originalSql,
            BoundSql boundSql,
            Object parameterObject )
            throws Throwable {
        String countSql = getCountSql(mappedStatement, originalSql);
        Connection connection = null;
        long totpage = 0;
        try {

            //如果在一个事物中先做了insert/delete 操作，考虑到innodb 的默认级别是可重复读，如果这里重新去获取一个连接拿到
            // 的这个总数是未执行insert /delete 之前的，显然不正确，但是不推荐把增删和读取放在一个事物里面
            SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager
                    .getResource(sqlSessionFactory);
            if (holder != null) {
                SqlSession sqlSession = holder.getSqlSession();
                if (null != sqlSession) {
                    connection = sqlSession.getConnection();
                }
            } else {

                connection =
                        mappedStatement.getConfiguration().getEnvironment().getDataSource()
                                .getConnection();
            }

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
            if (!TransactionSynchronizationManager.isSynchronizationActive()) {
                connection.close();
            }
        }

        return totpage;
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     */
    private String getCountSql( MappedStatement mappedStatement, String sql ) {
        String countSqlByAnnotation = getCountSqlByAnnotation(mappedStatement);
        return !Strings.isNullOrEmpty(countSqlByAnnotation)
                ? countSqlByAnnotation
                : countSqlParser.getSmartCountSql(sql);
    }

    private String getCountSqlByAnnotation( MappedStatement mappedStatement ) {
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

    public void setSqlSessionFactory( SqlSessionFactory sqlSessionFactory ) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
