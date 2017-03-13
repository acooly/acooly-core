package com.acooly.module.ds.check.loader;


import com.acooly.module.ds.check.dic.Column;
import com.acooly.module.ds.check.dic.Table;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shuijing
 */
public class MysqlTableLoader implements TableLoader {

    private static final Logger logger = LoggerFactory.getLogger(MysqlTableLoader.class);

    /**
     * 列相关元数据SQL
     */
    protected final static String COLUMN_METADATA_SQL = "select COLUMN_NAME as name,DATA_TYPE as type,(case when data_type='varchar' then CHARACTER_MAXIMUM_LENGTH else NUMERIC_PRECISION end)as length, IS_NULLABLE AS nullable,COLUMN_COMMENT AS comments,COLUMN_DEFAULT AS defaultValue,EXTRA AS extra from information_schema.COLUMNS where table_schema=? and table_name=? order by ORDINAL_POSITION";
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
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(COLUMN_METADATA_SQL);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            ResultSet rs = stmt.executeQuery();
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
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                    // ig
                }
            }
        }
    }

    @Override
    public List<String> getTableNames(String schema) {
        List<String> tables = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TABLES);
            stmt.setString(1, schema);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("获取表名失败：" + e.getMessage(), e);
        } finally {
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
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_VERSION);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return checkVersion(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("获取数据库版本失败：" + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
        return false;
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
