package com.acooly.module.ds.check.loader;

import com.acooly.module.ds.check.dic.Table;

import java.util.List;

/**
 * @author shuijing
 */
public interface TableLoader {

    /**
     * 查询表信息
     * @param schema 数据库名
     * @param tableName 表名
     */
    Table loadTable(String schema, String tableName);

    /**
     * 查询所有表
     * @param schema 数据库名
     */
    List<String> getTableNames(String schema);

    /**
     * 检查数据库版本
     */
    boolean checkDatabaseVersion();

    /**
     * 检查字段
     */
    boolean checkTableColums(Table table);
}
