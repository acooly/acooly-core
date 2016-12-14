package com.acooly.core.common.dao.mybatis.typehandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.acooly.core.utils.Money;

/**
 * Money 枚举映射转换
 * 
 * 使用Code写入值或设置值
 * 
 * @author zhangpu
 *
 */
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

	public MoneyTypeHandler(Class<Money> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType)
	        throws SQLException {
		ps.setBigDecimal(i, parameter.getAmount());
	}

	@Override
	public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		BigDecimal bd = cs.getBigDecimal(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			return new Money(bd);
		}
	}

	@Override
	public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		BigDecimal bd = rs.getBigDecimal(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return new Money(bd);
		}
	}

	@Override
	public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
		BigDecimal bd = rs.getBigDecimal(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return new Money(bd);
		}
	}

}
