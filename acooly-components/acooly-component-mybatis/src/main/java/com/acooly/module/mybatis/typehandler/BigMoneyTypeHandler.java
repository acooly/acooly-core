package com.acooly.module.mybatis.typehandler;

import com.acooly.core.utils.BigMoney;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BigMoney 枚举映射转换
 *
 * <p>使用Code写入值或设置值
 *
 * @author zhangpu
 */
@MappedTypes(BigMoney.class)
@MappedJdbcTypes({JdbcType.BIGINT, JdbcType.DECIMAL, JdbcType.NUMERIC})
public class BigMoneyTypeHandler extends BaseTypeHandler<BigMoney> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigMoney parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setBigDecimal(i, parameter.getAmount());
    }

    @Override
    public BigMoney getNullableResult(ResultSet rs, String columnName) throws SQLException {
        BigDecimal result = rs.getBigDecimal(columnName);
        return result == null ? null : BigMoney.valueOf(result);
    }

    @Override
    public BigMoney getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal result = rs.getBigDecimal(columnIndex);
        return result == null ? null : BigMoney.valueOf(result);
    }

    @Override
    public BigMoney getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        BigDecimal result = cs.getBigDecimal(columnIndex);
        return result == null ? null : BigMoney.valueOf(result);
    }
}
