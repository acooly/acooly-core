package com.acooly.core.utils.conversion;

import java.util.Arrays;
import java.util.List;

/**
 * {@link Enum}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class EnumTypeConverter extends AbstractTypeConverter<Enum<? extends Enum<?>>> {

  @SuppressWarnings("unchecked")
  private static Enum<?> enumValue0(Class<? extends Enum> toClass, Object o) {
    Enum<?> result = null;
    if (o == null) {
      result = null;
    } else if (o instanceof String[]) {
      result = Enum.valueOf(toClass, ((String[]) o)[0]);
    } else if (o instanceof String) {
      result = Enum.valueOf(toClass, (String) o);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> E enumValue(Class<E> toClass, Object o) {
    return (E) enumValue0(toClass, o);
  }

  @SuppressWarnings("unchecked")
  public Class<Enum<? extends Enum<?>>> getTargetType() {
    Class<?> enumClass = Enum.class;
    return (Class<Enum<? extends Enum<?>>>) enumClass;
  }

  public List<Class<?>> getSupportedSourceTypes() {
    return Arrays.asList(CharSequence.class, String[].class);
  }

  public Enum<? extends Enum<?>> convert(
      Object value, Class<? extends Enum<? extends Enum<?>>> toType) {
    try {
      return (Enum<? extends Enum<?>>) enumValue0(toType, value);
    } catch (Exception e) {
      throw new TypeConversionException(e);
    }
  }
}
