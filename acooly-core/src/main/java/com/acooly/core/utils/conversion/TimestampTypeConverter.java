package com.acooly.core.utils.conversion;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * {@link Timestamp}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class TimestampTypeConverter extends AbstractTypeConverter<Timestamp> {

  public Class<Timestamp> getTargetType() {
    return Timestamp.class;
  }

  public List<Class<?>> getSupportedSourceTypes() {
    return Arrays.asList(CharSequence.class, String[].class, Date.class);
  }

  public Timestamp convert(Object value, Class<? extends Timestamp> toType) {
    try {
      return (Timestamp) DateTypeConverter.dateValue(value, toType);
    } catch (ClassCastException e) {
      throw new TypeConversionException(e);
    }
  }
}
