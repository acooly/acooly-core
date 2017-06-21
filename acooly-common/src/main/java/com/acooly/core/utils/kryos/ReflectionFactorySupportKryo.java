/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-24 00:26 创建
 */
package com.acooly.core.utils.kryos;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author qiubo */
public class ReflectionFactorySupportKryo extends Kryo {

  private static final ReflectionFactory REFLECTION_FACTORY =
      ReflectionFactory.getReflectionFactory();
  private static final Object[] INITARGS = new Object[0];

  private static final Map<Class<?>, Constructor<?>> _constructors =
      new ConcurrentHashMap<Class<?>, Constructor<?>>();

  private static Object newInstanceFrom(final Constructor<?> constructor) {
    try {
      return constructor.newInstance(INITARGS);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T newInstanceFromReflectionFactory(final Class<T> type) {
    Constructor<?> constructor = _constructors.get(type);
    if (constructor == null) {
      constructor = newConstructorForSerialization(type);
      _constructors.put(type, constructor);
    }
    return (T) newInstanceFrom(constructor);
  }

  private static <T> Constructor<?> newConstructorForSerialization(final Class<T> type) {
    try {
      final Constructor<?> constructor =
          REFLECTION_FACTORY.newConstructorForSerialization(
              type, Object.class.getDeclaredConstructor(new Class[0]));
      constructor.setAccessible(true);
      return constructor;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Constructor<?> getNoArgsConstructor(final Class<?> type) {
    final Constructor<?>[] constructors = type.getConstructors();
    for (final Constructor<?> constructor : constructors) {
      if (constructor.getParameterTypes().length == 0) {
        constructor.setAccessible(true);
        return constructor;
      }
    }
    return null;
  }

  @Override
  public Serializer<?> getDefaultSerializer(@SuppressWarnings("rawtypes") final Class type) {
    final Serializer<?> result = super.getDefaultSerializer(type);
    if (result instanceof FieldSerializer) {
      ((FieldSerializer<?>) result).setIgnoreSyntheticFields(false);
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T newInstance(final Class<T> type) {
    if (type == null) {
      throw new IllegalArgumentException("type cannot be null.");
    }
    Constructor<?> constructor = _constructors.get(type);
    if (constructor == null) {
      constructor = getNoArgsConstructor(type);
      if (constructor == null) {
        constructor = newConstructorForSerialization(type);
      }
      _constructors.put(type, constructor);
    }
    return (T) newInstanceFrom(constructor);
  }
}
