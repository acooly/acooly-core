package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Reflections;

import java.util.*;

/**
 * {@link Set}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class SetTypeConverter extends AbstractTypeConverter<Set<?>> {

  @SuppressWarnings("unchecked")
  public static <E extends Set<?>> E setValue(Object value, Class<? extends E> setClassType) {
    Set<Object> set = null;
    if ((Class<?>) setClassType == Set.class) {
      // 使用 HashSet 作为实现
      set = new HashSet<Object>();
    } else { // 如果是具体的类则使用该类的类型
      try {
        set = (Set<Object>) Reflections.createObject(setClassType);
      } catch (Exception e) {
        throw new TypeConversionException(e);
      }
    }
    set.addAll((List<Object>) value);
    return (E) set;
  }

  @SuppressWarnings("unchecked")
  public Class<Set<?>> getTargetType() {
    Class<?> setClass = Set.class;
    return (Class<Set<?>>) setClass;
  }

  public List<Class<?>> getSupportedSourceTypes() {
    return Arrays.asList(Collection.class, Object[].class);
  }

  public Set<?> convert(Object value, Class<? extends Set<?>> toType) {
    return setValue(value, toType);
  }
}
