package com.acooly.module.ds.check;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.dao.support.DataSourceReadyEvent;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.DruidProperties;
import com.acooly.module.ds.check.dic.Column;
import com.acooly.module.ds.check.dic.Table;
import com.acooly.module.ds.check.loader.TableLoader;
import com.acooly.module.ds.check.loader.TableLoaderProvider;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author shuijing
 */
public class DatabaseTableChecker implements ApplicationListener<DataSourceReadyEvent>, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(DatabaseTableChecker.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("DatabaseTableCheckThread"));

    private Map<String, String> excludedDatabaseTables;

    private DruidProperties druidProperties;

    //沒有通过检查的表
    private List<String> tableNamesNotPassCheck = new ArrayList<>();

    public DatabaseTableChecker(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
        this.excludedDatabaseTables = druidProperties.getChecker().getExcludedColumnTables();
    }

    @Override
    public void onApplicationEvent(DataSourceReadyEvent event) {
        //tofix 在开发者模式下 AbstractDatabaseScriptIniter 同样监听DataSourceReadyEvent
        //      在执行数据库脚本初始化未完成的时候 执行了表检查 第一次检查结果会不准确

        //开发者模式下检查
        if (!Apps.isDevMode()) {
            return;
        }
        executorService.execute(() -> {
            DataSource dataSource = (DataSource) event.getSource();
            try {
                TableLoader tableLoader = TableLoaderProvider.getTableLoader(dataSource);
                String schema = getMysqlschema(dataSource);

                //检查数据库版本
                boolean passVersion = tableLoader.checkDatabaseVersion();
                if (!passVersion) {
                    throw new AppConfigException("为正常运行，mysql数据库版本5.6以上版本 !!!");
                }
                //先获取所有表名
                List<String> tableNames = tableLoader.getTableNames(schema);
                //去掉排除的表
                String excludedDatabaseStr = excludedDatabaseTables.values().toString();
                tableNames.removeAll(Splitter.on(',').trimResults().omitEmptyStrings().splitToList(excludedDatabaseStr.substring(1, excludedDatabaseStr.length() - 1)));

                tableNames.forEach(tableName -> {
                    //获取表信息
                    Table table = tableLoader.loadTable(schema, tableName);
                    //获取列信息并比较
                    boolean hasColums = tableLoader.checkTableColums(table);
                    if (!hasColums) {
                        tableNamesNotPassCheck.add(tableName);
                    }
                });
                //输出检查没通过的表名
                if (!tableNamesNotPassCheck.isEmpty()) {
                    // 是否退出 System.exit(0);
                    StringBuffer expMsg = new StringBuffer();
                    expMsg.append("数据库表检查，有")
                        .append(tableNamesNotPassCheck.size())
                        .append("个表没有按照标准设置id或createTime或updateTime:")
                        .append(tableNamesNotPassCheck.toString()).append("\n")
                        .append("如果是关联用的表或者系统表，请用例如'acooly.ds.Checker.excludedColumnTables.${componentName}=sys_role_resc,sys_user_role' 排除掉\n")
                        .append("标准的id、createTime、updateTime 格式分别为：\n")
                        .append("id  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID' \n")
                        .append("create_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'\n")
                        .append("update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'");
                    throw new AppConfigException(expMsg.toString());
                }
            } catch (SQLException e) {
                logger.error("异步检查所有表结构异常", e.getMessage());
                throw new AppConfigException(e);
            }
        });
    }

    private  String getMysqlschema(DataSource dataSource) throws SQLException {
        String scheme = StringUtils.substringAfterLast(dataSource.getConnection().getMetaData().getURL(), "/");
        if (StringUtils.contains(scheme, "?")) {
            scheme = StringUtils.substringBefore(scheme, "?");
        }
        return scheme;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdownNow();
    }
}
