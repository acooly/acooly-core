package com.acooly.module.ds.check;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.DruidProperties;
import com.acooly.module.ds.check.dic.Column;
import com.acooly.module.ds.check.loader.TableLoader;
import com.acooly.module.ds.check.loader.TableLoaderProvider;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class DBPatchChecker {
    private ExecutorService executorService;
    private DruidProperties druidProperties;

    public DBPatchChecker(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
    }

    public void check(DataSource dataSource) {
        if (druidProperties.getDbPatchs() == null || druidProperties.getDbPatchs().isEmpty()) {
            return;
        }
        executorService =
                Executors.newSingleThreadExecutor(
                        new CustomizableThreadFactory("DatabasePatchCheckThread"));
        executorService.submit(
                () -> {
                    try {
                        TableLoader tableLoader = TableLoaderProvider.getTableLoader(dataSource);
                        String schema = getMysqlschema(dataSource);
                        Map<String, List<Column>> allTables = tableLoader.loadAllTables(schema);
                        Map<String, DBPatch> dbPatchs = druidProperties.getDbPatchs();
                        for (Map.Entry<String, DBPatch> entry : dbPatchs.entrySet()) {
                            List<Column> columns = allTables.get(entry.getKey());
                            if (CollectionUtils.isEmpty(columns)) {
                                log.error("业务缺少表:{}，请增加此表", entry.getKey());
                                System.exit(0);
                            }
                            Set<String> existsColums =
                                    columns.stream().map(column -> column.getName()).collect(Collectors.toSet());
                            List<String> missingColums = Lists.newArrayList();
                            entry
                                    .getValue()
                                    .getColumnName()
                                    .forEach(
                                            s -> {
                                                if (!existsColums.contains(s)) {
                                                    missingColums.add(s);
                                                }
                                            });
                            if (!missingColums.isEmpty()) {
                                log.warn(
                                        "表:{}缺少以下字段：{}，自动执行下列sql语句:\n{}",
                                        entry.getKey(),
                                        missingColums,
                                        entry.getValue().getPatchSql());
                                try {
                                    executeSql(dataSource, entry.getValue().getPatchSql());
                                } catch (Exception e) {
                                    log.error(
                                            "自动执行sql失败，表:{}缺少以下字段：{}，请按照提示执行sql语句:\n{}",
                                            entry.getKey(),
                                            missingColums,
                                            entry.getValue().getPatchSql());
                                    System.exit(0);
                                }

                            }
                        }

                    } catch (SQLException e) {
                        log.error("执行表字段检查失败", e);
                    }
                });
    }

    private void executeSql(DataSource dataSource, String sql) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ScriptUtils.executeSqlScript(connection, new ByteArrayResource(sql.getBytes(Charsets.UTF_8)));
        } catch (SQLException e) {
            throw new AppConfigException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.warn("", e);
                }
            }
        }
    }

    private String getMysqlschema(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String scheme =
                    StringUtils.substringAfterLast(connection.getMetaData().getURL(), "/");
            if (StringUtils.contains(scheme, "?")) {
                scheme = StringUtils.substringBefore(scheme, "?");
            }
            return scheme;
        }
    }
}
