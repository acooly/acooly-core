package com.acooly.module.mybatis.typehandler;

import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-18 11:40
 */
@MappedTypes(DBMap.class)
@MappedJdbcTypes({JdbcType.CHAR, JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
public class DBMapTypeHandler extends BaseTypeHandler<DBMap> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DBMap parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.toJson());
    }

    @Override
    public DBMap getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return getMap(value);
    }

    @Override
    public DBMap getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return getMap(value);
    }


    @Override
    public DBMap getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return getMap(value);
    }

    private DBMap getMap(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return DBMap.fromJson(value);
    }
}

