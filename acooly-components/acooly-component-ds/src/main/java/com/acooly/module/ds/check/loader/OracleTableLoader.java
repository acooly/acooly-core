package com.acooly.module.ds.check.loader;


import com.acooly.module.ds.check.dic.Column;
import com.acooly.module.ds.check.dic.Table;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author shuijing
 */
public class OracleTableLoader implements TableLoader {

    private static final Logger logger = LoggerFactory.getLogger(OracleTableLoader.class);

    @Getter
    private DataSource dataSource;

    public OracleTableLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Table loadTable(String schema, String tableName) {
        return null;
    }

    @Override
    public Map<String, List<Column>> loadAllTables(String schema) {
        return null;
    }

    @Override
    public List<String> getTableNames(String schema) {
        return null;
    }

    @Override
    public boolean checkDatabaseVersion() {
        return false;
    }

    @Override
    public boolean checkTableColums(List<Column> columns) {
        return false;
    }


}
