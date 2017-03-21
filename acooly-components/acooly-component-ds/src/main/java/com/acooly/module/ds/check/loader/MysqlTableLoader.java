package com.acooly.module.ds.check.loader;


import com.acooly.module.ds.check.dic.Column;
import com.acooly.module.ds.check.dic.Table;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author shuijing
 */
public class MysqlTableLoader implements TableLoader {

    private static final Logger logger = LoggerFactory.getLogger(MysqlTableLoader.class);

    /**
     * 列相关元数据SQL
     */
    protected final static String COLUMN_METADATA_SQL = "select COLUMN_NAME as name,DATA_TYPE as type,(case when data_type='varchar' then CHARACTER_MAXIMUM_LENGTH else NUMERIC_PRECISION end)as length, IS_NULLABLE AS nullable,COLUMN_COMMENT AS comments,COLUMN_DEFAULT AS defaultValue,EXTRA AS extra from information_schema.COLUMNS where table_schema=? and table_name=? order by ORDINAL_POSITION";

    protected final static String ALL_COLUMN_METADATA_SQL = "select COLUMN_NAME as name,DATA_TYPE as type,(case when data_type='varchar' then CHARACTER_MAXIMUM_LENGTH else NUMERIC_PRECISION end)as length, IS_NULLABLE AS nullable,COLUMN_COMMENT AS comments,COLUMN_DEFAULT AS defaultValue,EXTRA AS extra,table_name AS tableName from information_schema.COLUMNS where table_schema=?";

    /**
     * 获取所有的表名SQL
     */
    protected final static String SELECT_ALL_TABLES = "select TABLE_NAME from information_schema.tables where table_schema=? ORDER BY TABLE_NAME";

    protected final static String SELECT_VERSION = "SELECT version()";


    @Getter
    private DataSource dataSource;

    public MysqlTableLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Table loadTable(String schema, String tableName) {
        Table tableMetadata = new Table();
        tableMetadata.setName(tableName);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(COLUMN_METADATA_SQL);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rs = stmt.executeQuery();
            List<Column> columnMetadatas = new LinkedList<Column>();
            Column columnMetadata;
            while (rs.next()) {
                columnMetadata = new Column();
                String name = rs.getString("name");
                columnMetadata.setName(name);
                columnMetadata.setLength(rs.getInt("length"));
                columnMetadata.setNullable(rs.getString("nullable").equalsIgnoreCase("YES"));
                String comment = rs.getString("comments");
                //列备注
                //comment = parseCanonicalComment(comment);
                columnMetadata.setCommon(StringUtils.isBlank(comment) ? name : comment);
                Object defaultValue = rs.getObject("defaultValue");
                columnMetadata.setDefaultValue(defaultValue);
                columnMetadata.setDataType(rs.getString("type"));

                columnMetadata.setExtra(rs.getString("extra"));

                columnMetadatas.add(columnMetadata);
            }
            if (columnMetadatas == null || columnMetadatas.size() == 0) {
                throw new RuntimeException("表不存在或没有正确定义");
            }
            tableMetadata.setColumns(columnMetadatas);
            //logger.info("Load table metadata: " + tableName);
            rs.close();
            stmt.close();
            return tableMetadata;
        } catch (Exception e) {
            throw new RuntimeException("获取表名失败：" + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
    }


    @Override
    public Map<String, List<Column>> loadAllTables(String schema) {

        Map<String, List<Column>> tableMap = Maps.newHashMap();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(ALL_COLUMN_METADATA_SQL);
            stmt.setString(1, schema);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("tableName");
                if (tableMap.containsKey(tableName)) {
                    tableMap.get(tableName).add(getColumn(rs));
                } else {
                    //行集合
                    List<Column> newTableCols = new ArrayList<>();
                    //一行
                    newTableCols.add(getColumn(rs));
                    tableMap.put(tableName, newTableCols);
                }
            }
            if (tableMap.entrySet().isEmpty()) {
                throw new RuntimeException("表不存在或没有正确定义");
            }
            //logger.info("Load table metadata: " + tableName);
            return tableMap;
        } catch (Exception e) {
            throw new RuntimeException("获取表名失败：" + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    private Column getColumn(ResultSet rs) throws SQLException {
        Column columnMetadata = new Column();
        String name = rs.getString("name");
        columnMetadata.setName(name);
        columnMetadata.setLength(rs.getInt("length"));
        columnMetadata.setNullable(rs.getString("nullable").equalsIgnoreCase("YES"));
        String comment = rs.getString("comments");
        //列备注
        //comment = parseCanonicalComment(comment);
        columnMetadata.setCommon(StringUtils.isBlank(comment) ? name : comment);
        Object defaultValue = rs.getObject("defaultValue");
        columnMetadata.setDefaultValue(defaultValue);
        columnMetadata.setDataType(rs.getString("type"));
        columnMetadata.setExtra(rs.getString("extra"));
        return columnMetadata;
    }


    @Override
    public List<String> getTableNames(String schema) {
        List<String> tables = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_TABLES);
            stmt.setString(1, schema);
            rs = stmt.executeQuery();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("获取表名失败：" + e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
        return tables;
    }

    @Override
    public boolean checkDatabaseVersion() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SELECT_VERSION);
            rs = stmt.executeQuery();
            while (rs.next()) {
                return checkVersion(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("获取数据库版本失败：" + e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
        return false;
    }

    /**
     * 检查id、createTime、updateTime 格式分别为：
     * id  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     * create_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
     */
    @Override
    public boolean checkTableColums(List<Column> columns) {
        boolean isIdValid = false;
        boolean hasCreateTime = false;
        boolean hasUpdateTime = false;
        for (Column column : columns) {
            String name = column.getName();
            String type = column.getDataType();
            String defaultValue = String.valueOf(column.getDefaultValue());
            String extra = column.getExtra();
            //检查id
            //bigint(20)和bigint(10)在存储上长度都为 8 bytes
            //@see https://dev.mysql.com/doc/refman/5.7/en/integer-types.html
            if (StringUtils.equalsIgnoreCase("id", name) && StringUtils.equalsIgnoreCase("bigint", type) && StringUtils.equalsIgnoreCase("auto_increment", extra)) {
                isIdValid = true;
                continue;
            }
            //检查createTime
            if (StringUtils.equalsIgnoreCase("timestamp", type) && StringUtils.equalsIgnoreCase("CURRENT_TIMESTAMP", defaultValue)) {
                if (StringUtils.equalsIgnoreCase("create_time", name) || StringUtils.equalsIgnoreCase("raw_add_time", name)) {
                    hasCreateTime = true;
                    continue;
                }
            }
            //检查updateTime
            if (StringUtils.equalsIgnoreCase("timestamp", type) && StringUtils.equalsIgnoreCase("CURRENT_TIMESTAMP", defaultValue) && StringUtils.equalsIgnoreCase("ON update CURRENT_TIMESTAMP", extra)) {
                if (StringUtils.equalsIgnoreCase("update_time", name) || StringUtils.equalsIgnoreCase("raw_update_time", name)) {
                    hasUpdateTime = true;
                }
            }
        }
        return isIdValid && hasCreateTime && hasUpdateTime;
    }

    private boolean checkVersion(String version) {
        //5.7.15-9-log
        String[] versionSplit = version.split("\\.");
        Integer bigVersion = Integer.valueOf(versionSplit[0]);
        Integer smallVersion = Integer.valueOf(versionSplit[1]);
        if (bigVersion < 5) {
            return false;
        }
        if (smallVersion < 6) {
            return false;
        }
        return true;
    }

}
