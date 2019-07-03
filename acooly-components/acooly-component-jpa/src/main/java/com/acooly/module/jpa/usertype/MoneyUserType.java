package com.acooly.module.jpa.usertype;

import com.acooly.core.utils.Money;
import org.hibernate.HibernateException;
import org.hibernate.annotations.TypeDef;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author qiubo
 */
@TypeDef(defaultForType = Money.class, typeClass = MoneyUserType.class)
public class MoneyUserType implements UserType {

    private static final int[] SQL_TYPES = {Types.INTEGER};

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if ((x == null) || (y == null)) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Long value = resultSet.getLong(strings[0]);
        Money m = new Money();
        m.setCent(value);
        return m;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o == null) {
            preparedStatement.setLong(i, 0L);
            return;
        }

        if (!(o instanceof Money)) {
            throw new HibernateException("value is not type of " + Money.class);
        }

        preparedStatement.setLong(i, ((Money) o).getCent());
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value instanceof Money && value != null) {
            return new Money(((Money) value).getAmount());
        }
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }


}
