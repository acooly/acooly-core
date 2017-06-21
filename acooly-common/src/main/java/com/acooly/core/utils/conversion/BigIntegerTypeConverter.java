package com.acooly.core.utils.conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * {@link BigInteger}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class BigIntegerTypeConverter extends AbstractTypeConverter<BigInteger> {

  public static BigInteger bigIntValue(Object value) throws NumberFormatException {
    if (value == null) {
      return null;
    }
    Class<?> c = value.getClass();
    if (c == BigInteger.class) {
      return (BigInteger) value;
    }
    if (c == BigDecimal.class) {
      return ((BigDecimal) value).toBigInteger();
    }
    if (c.getSuperclass() == Number.class) {
      return BigInteger.valueOf(((Number) value).longValue());
    }
    if (c == Boolean.class) {
      return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
    }
    if (c == Character.class) {
      return BigInteger.valueOf(((Character) value).charValue());
    }
    return new BigInteger(StringTypeConverter.stringValue(value, true));
  }

  public Class<BigInteger> getTargetType() {
    return BigInteger.class;
  }

  public List<Class<?>> getSupportedSourceTypes() {
    return Arrays.asList(
        Number.class,
        Boolean.class,
        Character.class,
        CharSequence.class,
        CharSequence[].class,
        BigDecimal.class);
  }

  public BigInteger convert(Object value, Class<? extends BigInteger> toType) {
    try {
      return bigIntValue(value);
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }
}
