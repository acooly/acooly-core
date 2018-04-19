package com.acooly.module.mybatis.typehandler;

import com.acooly.core.utils.mapper.JsonMapper;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-18 11:40
 */
@MappedTypes(Map.class)
@MappedJdbcTypes({JdbcType.CHAR, JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
public class MapTypeHandler extends BaseTypeHandler<Map> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, JsonMapper.nonEmptyMapper().toJson(parameter));
    }

    @Override
    public Map getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return getMap(value);
    }

    @Override
    public Map getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return getMap(value);
    }


    @Override
    public Map getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return getMap(value);
    }

    private Map getMap(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return JsonMapper.nonEmptyMapper().fromJson(value, Map.class);
    }
}

