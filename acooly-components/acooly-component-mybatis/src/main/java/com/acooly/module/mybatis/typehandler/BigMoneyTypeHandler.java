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
        if (parameter.getDbMode() == BigMoney.DB_MODE_BIG_DECIMAL) {
            ps.setBigDecimal(i, parameter.getAmount());
        } else {
            ps.setLong(i, parameter.getCent());
        }
    }

    @Override
    public BigMoney getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        return doDeserialize(object);
    }

    @Override
    public BigMoney getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object object = rs.getObject(columnIndex);
        return doDeserialize(object);
    }

    @Override
    public BigMoney getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object object = cs.getObject(columnIndex);
        return doDeserialize(object);
    }

    protected BigMoney doDeserialize(Object object) {
        if (object == null) {
            return null;
        }
        if (BigDecimal.class.isAssignableFrom(object.getClass())) {
            return BigMoney.valueOf((BigDecimal) object);
        } else {
            return BigMoney.centOf((Long) object);
        }
    }
}
