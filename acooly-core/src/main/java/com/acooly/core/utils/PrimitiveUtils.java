/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本类型工具。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public abstract class PrimitiveUtils {

  /** 空的 int 数组 */
  public static final int[] EMPTY_INTS = new int[0];
  /** 空的 long 数组 */
  public static final long[] EMPTY_LONGS = new long[0];
  /** 空的 short 数组 */
  public static final short[] EMPTY_SHORTS = new short[0];
  /** 空的float 数组 */
  public static final float[] EMPTY_FLOATS = new float[0];
  /** 空的 double 数组 */
  public static final double[] EMPTY_DOUBLES = new double[0];
  /** 空的 byte 数组 */
  public static final byte[] EMPTY_BYTES = new byte[0];
  /** 空的 char 数组 */
  public static final char[] EMPTY_CHARS = new char[0];
  /** 空的 boolean 数组 */
  public static final boolean[] EMPTY_BOOLEANS = new boolean[0];
  /** 空的 {@link Integer} 数组 */
  public static final Integer[] EMPTY_INT_WRAPPERS = new Integer[0];
  /** 空的 {@link Long} 数组 */
  public static final Long[] EMPTY_LONG_WRAPPERS = new Long[0];
  /** 空的 {@link Short} 数组 */
  public static final Short[] EMPTY_SHORT_WRAPPERS = new Short[0];
  /** 空的 {@link Float} 数组 */
  public static final Float[] EMPTY_FLOAT_WRAPPERS = new Float[0];
  /** 空的 {@link Double} 数组 */
  public static final Double[] EMPTY_DOUBLE_WRAPPERS = new Double[0];
  /** 空的 {@link Byte} 数组 */
  public static final Byte[] EMPTY_BYTE_WRAPPERS = new Byte[0];
  /** 空的 {@link Character} 数组 */
  public static final Character[] EMPTY_CHAR_WRAPPERS = new Character[0];
  /** 空的 {@link Boolean} 数组 */
  public static final Boolean[] EMPTY_BOOLEAN_WRAPPERS = new Boolean[0];

  private static final Map<String, TypeHolder> PRIMITIVES = new HashMap<String, TypeHolder>();

  static {
    PRIMITIVES.put(Byte.TYPE.getName(), new ByteTypeHolder());
    PRIMITIVES.put(Character.TYPE.getName(), new CharTypeHolder());
    PRIMITIVES.put(Short.TYPE.getName(), new ShortTypeHolder());
    PRIMITIVES.put(Integer.TYPE.getName(), new IntTypeHolder());
    PRIMITIVES.put(Long.TYPE.getName(), new LongTypeHolder());
    PRIMITIVES.put(Float.TYPE.getName(), new FloatTypeHolder());
    PRIMITIVES.put(Double.TYPE.getName(), new DoubleTypeHolder());
    PRIMITIVES.put(Boolean.TYPE.getName(), new BooleanTypeHolder());
  }

  /**
   * 得到包装器{@link Short}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 short 的默认值 0。
   */
  public static short value(Short value) {
    if (value == null) {
      return (short) 0;
    }
    return value.shortValue();
  }

  /**
   * 得到包装器{@link Integer}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 int 的默认值 0。
   */
  public static int value(Integer value) {
    if (value == null) {
      return 0;
    }
    return value.intValue();
  }

  /**
   * 得到包装器{@link Long}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 long 的默认值 0L。
   */
  public static long value(Long value) {
    if (value == null) {
      return 0l;
    }
    return value.longValue();
  }

  /**
   * 得到包装器{@link Float}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 float 的默认值 0f。
   */
  public static float value(Float value) {
    if (value == null) {
      return 0.0f;
    }
    return value.floatValue();
  }

  /**
   * 得到包装器{@link Double}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 double 的默认值 0。
   */
  public static double value(Double value) {
    if (value == null) {
      return 0.0d;
    }
    return value.doubleValue();
  }

  /**
   * 得到包装器{@link Byte}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 byte 的默认值 0。
   */
  public static byte value(Byte value) {
    if (value == null) {
      return (byte) 0;
    }
    return value.byteValue();
  }

  /**
   * 得到包装器{@link Boolean}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 boolean 的默认值 false。
   */
  public static boolean value(Boolean value) {
    if (value == null) {
      return false;
    }
    return value.booleanValue();
  }

  /**
   * 得到包装器{@link Character}中包装的值 ,如果包装器为 null ,则返回默认值。
   *
   * @param value 包装器。
   * @return 包装器中包装的值，如果包装器为 null ，则返回 char 的默认值 \u0000。
   */
  public static char value(Character value) {
    if (value == null) {
      return '\u0000';
    }
    return value.charValue();
  }

  /**
   * 包装 shrot 的值为 {@link Short} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Short value(short value) {
    return Short.valueOf(value);
  }

  /**
   * 包装 int 的值为 {@link Integer} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Integer value(int value) {
    return Integer.valueOf(value);
  }

  /**
   * 包装 long 的值为 {@link Long} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Long value(long value) {
    return Long.valueOf(value);
  }

  /**
   * 包装 float 的值为 {@link Float} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Float value(float value) {
    return Float.valueOf(value);
  }

  /**
   * 包装 double 的值为 {@link Double} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Double value(double value) {
    return Double.valueOf(value);
  }

  /**
   * 包装 byte 的值为 {@link Byte} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Byte value(byte value) {
    return Byte.valueOf(value);
  }

  /**
   * 包装 boolean 的值为 {@link Boolean} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Boolean value(boolean value) {
    return Boolean.valueOf(value);
  }

  /**
   * 包装 value 的值为 {@link Character} 。
   *
   * @param value 值。
   * @return 值对应的包装器。
   */
  public static Character value(char value) {
    return Character.valueOf(value);
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Integer)
   */
  public static int[] values(Integer[] values) {
    if (values == null) {
      return EMPTY_INTS;
    }
    int[] intValues = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      intValues[i] = value(values[i]);
    }
    return intValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Long)
   */
  public static long[] values(Long[] values) {
    if (values == null) {
      return EMPTY_LONGS;
    }
    long[] longValues = new long[values.length];
    for (int i = 0; i < values.length; i++) {
      longValues[i] = value(values[i]);
    }
    return longValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Short)
   */
  public static short[] values(Short[] values) {
    if (values == null) {
      return EMPTY_SHORTS;
    }
    short[] shortValues = new short[values.length];
    for (int i = 0; i < values.length; i++) {
      shortValues[i] = value(values[i]);
    }
    return shortValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Float)
   */
  public static float[] values(Float[] values) {
    if (values == null) {
      return EMPTY_FLOATS;
    }
    float[] floatValues = new float[values.length];
    for (int i = 0; i < values.length; i++) {
      floatValues[i] = value(values[i]);
    }
    return floatValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Double)
   */
  public static double[] values(Double[] values) {
    if (values == null) {
      return EMPTY_DOUBLES;
    }
    double[] doubleValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      doubleValues[i] = value(values[i]);
    }
    return doubleValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Boolean)
   */
  public static boolean[] values(Boolean[] values) {
    if (values == null) {
      return EMPTY_BOOLEANS;
    }
    boolean[] booleanValues = new boolean[values.length];
    for (int i = 0; i < values.length; i++) {
      booleanValues[i] = value(values[i]);
    }
    return booleanValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Byte)
   */
  public static byte[] values(Byte[] values) {
    if (values == null) {
      return EMPTY_BYTES;
    }
    byte[] byteValues = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      byteValues[i] = value(values[i]);
    }
    return byteValues;
  }

  /**
   * 得到包装器数组对应的基本类型数组。
   *
   * @param values 包装器数组。
   * @return 包装器数组对应的基本类型数组，如果包装器数组为 null ，则返回长度为0的对应基本类型的数组，如果包装器数组中的元素为 null
   *     ，则将该元素对应的基本类型默认值放入基本类型数组。
   * @see #value(Character)
   */
  public static char[] values(Character[] values) {
    if (values == null) {
      return EMPTY_CHARS;
    }
    char[] charValues = new char[values.length];
    for (int i = 0; i < values.length; i++) {
      charValues[i] = value(values[i]);
    }
    return charValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Integer[] values(int[] values) {
    if (values == null) {
      return EMPTY_INT_WRAPPERS;
    }
    Integer[] integerValues = new Integer[values.length];
    for (int i = 0; i < values.length; i++) {
      integerValues[i] = Integer.valueOf(values[i]);
    }
    return integerValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Long[] values(long[] values) {
    if (values == null) {
      return EMPTY_LONG_WRAPPERS;
    }
    Long[] longValues = new Long[values.length];
    for (int i = 0; i < values.length; i++) {
      longValues[i] = Long.valueOf(values[i]);
    }
    return longValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Short[] values(short[] values) {
    if (values == null) {
      return EMPTY_SHORT_WRAPPERS;
    }
    Short[] shortValues = new Short[values.length];
    for (int i = 0; i < values.length; i++) {
      shortValues[i] = Short.valueOf(values[i]);
    }
    return shortValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Float[] values(float[] values) {
    if (values == null) {
      return EMPTY_FLOAT_WRAPPERS;
    }
    Float[] floatValues = new Float[values.length];
    for (int i = 0; i < values.length; i++) {
      floatValues[i] = Float.valueOf(values[i]);
    }
    return floatValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Double[] values(double[] values) {
    if (values == null) {
      return EMPTY_DOUBLE_WRAPPERS;
    }
    Double[] doubleValues = new Double[values.length];
    for (int i = 0; i < values.length; i++) {
      doubleValues[i] = Double.valueOf(values[i]);
    }
    return doubleValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Boolean[] values(boolean[] values) {
    if (values == null) {
      return EMPTY_BOOLEAN_WRAPPERS;
    }
    Boolean[] booleanValues = new Boolean[values.length];
    for (int i = 0; i < values.length; i++) {
      booleanValues[i] = Boolean.valueOf(values[i]);
    }
    return booleanValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Byte[] values(byte[] values) {
    if (values == null) {
      return EMPTY_BYTE_WRAPPERS;
    }
    Byte[] byteValues = new Byte[values.length];
    for (int i = 0; i < values.length; i++) {
      byteValues[i] = Byte.valueOf(values[i]);
    }
    return byteValues;
  }

  /**
   * 得到基本类型数组对应的包装器数组。
   *
   * @param values 基本类型数组。
   * @return 基本类型数组对应的包装器数组，如果包装器数组为 null ，则返回长度为0的对应包装器的数组。
   */
  public static Character[] values(char[] values) {
    if (values == null) {
      return EMPTY_CHAR_WRAPPERS;
    }
    Character[] characterValues = new Character[values.length];
    for (int i = 0; i < values.length; i++) {
      characterValues[i] = Character.valueOf(values[i]);
    }
    return characterValues;
  }

  /**
   * 通过基本类型的名称得到对应的 {@link Class} 对象。
   *
   * @param primitiveName 基本类型名称。
   * @return primitiveName 对应的 {@link Class} 对象，如果没有则返回 null 。
   */
  public static Class<?> getPrimitiveClass(String primitiveName) {
    TypeHolder typeHolder = PRIMITIVES.get(primitiveName);
    if (typeHolder == null) {
      return null;
    }
    return typeHolder.getType();
  }

  /**
   * 通过基本类型的名称得到对应包装器的 {@link Class} 对象。
   *
   * @param primitiveName 基本类型名称。
   * @return primitiveName 对应的包装器的 {@link Class} 对象，如果没有则返回 null 。
   * @since 2.1
   */
  public static Class<?> getWrapperClass(String primitiveName) {
    TypeHolder typeHolder = PRIMITIVES.get(primitiveName);
    if (typeHolder == null) {
      return null;
    }
    return typeHolder.getWrapper();
  }

  /**
   * 得到所有基本类型的Class。
   *
   * @return 所有基本类型的Class数组。
   */
  public static Class<?>[] getAllPrimitiveClasses() {
    return new Class<?>[] {
      Byte.TYPE,
      Character.TYPE,
      Short.TYPE,
      Integer.TYPE,
      Long.TYPE,
      Float.TYPE,
      Double.TYPE,
      Boolean.TYPE
    };
  }

  /**
   * 得到所有基本类型数组的Class。
   *
   * @return 所有基本类型数组的Class数组。
   */
  public static Class<?>[] getAllPrimitiveArrayClasses() {
    return new Class<?>[] {
      byte[].class,
      char[].class,
      short[].class,
      int[].class,
      long[].class,
      float[].class,
      double[].class,
      boolean[].class
    };
  }

  /**
   * 得到所有基本类型包装器的Class。
   *
   * @return 所有基本类型包装器的Class数组。
   */
  public static Class<?>[] getAllWrapperClasses() {
    return new Class<?>[] {
      Byte.class,
      Character.class,
      Short.class,
      Integer.class,
      Long.class,
      Float.class,
      Double.class,
      Boolean.class
    };
  }

  /**
   * 得到所有基本类型包装器数组的Class。
   *
   * @return 所有基本类型包装器数组的Class数组。
   */
  public static Class<?>[] getAllWrapperArrayClasses() {
    return new Class<?>[] {
      Byte[].class,
      Character[].class,
      Short[].class,
      Integer[].class,
      Long[].class,
      Float[].class,
      Double[].class,
      Boolean[].class
    };
  }

  private static interface TypeHolder {

    Class<?> getType();

    Class<?> getWrapper();
  }

  private static class ByteTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Byte.TYPE;
    }

    public Class<?> getWrapper() {
      return Byte.class;
    }
  }

  private static class CharTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Character.TYPE;
    }

    public Class<?> getWrapper() {
      return Character.class;
    }
  }

  private static class ShortTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Short.TYPE;
    }

    public Class<?> getWrapper() {
      return Short.class;
    }
  }

  private static class IntTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Integer.TYPE;
    }

    public Class<?> getWrapper() {
      return Integer.class;
    }
  }

  private static class LongTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Long.TYPE;
    }

    public Class<?> getWrapper() {
      return Long.class;
    }
  }

  private static class FloatTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Float.TYPE;
    }

    public Class<?> getWrapper() {
      return Float.class;
    }
  }

  private static class DoubleTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Double.TYPE;
    }

    public Class<?> getWrapper() {
      return Double.class;
    }
  }

  private static class BooleanTypeHolder implements TypeHolder {

    public Class<?> getType() {
      return Boolean.TYPE;
    }

    public Class<?> getWrapper() {
      return Boolean.class;
    }
  }
}
