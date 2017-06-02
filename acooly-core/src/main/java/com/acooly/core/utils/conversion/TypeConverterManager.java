package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.Collections;

/**
 * 类型转换器管理器。
 *
 * <p>注意：由于数组是协变的，所以一维数组可以使用 Object[].class 表示。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * @see TypeConverter
 */
public interface TypeConverterManager {

  /** 表示可用作所有源类型“通配符”的 Class 。 */
  public static final Class<?> ALL_SOUECE_TYPE_CLASS = AllSoueceType.class;

  /**
   * 通过需要转换到的类型的 {@link Class} 得到对应的类型转换器。如果不存在则返回 则返回 {@link Collections#emptyList()} 。
   *
   * @param targetType 需要转换到的类型的 {@link Class}。
   * @return 需要转换到的类型的 {@link Class} 得到对应的类型转换器的 Collection 。
   */
  <T> Collection<TypeConverter<T>> getTypeConverter(Class<T> targetType);

  /**
   * 通过转换的源类型的 {@link Class}和需要转换到的类型的 {@link Class} 得到对应的类型转换器。如果不存在则返回 null。
   *
   * @param sourceType 转换的源类型的 {@link Class}。
   * @param targetType 需要转换到的类型的 {@link Class}。
   * @return 需要转换到的类型的 {@link Class} 得到对应的类型转换器的 Collection 。
   */
  <S, T> TypeConverter<T> getTypeConverter(Class<S> sourceType, Class<T> targetType);

  /**
   * 以源类型的 {@link Class}和需要转换到的类型的 {@link Class}为 key
   * ，以类型转换器实例为值，注册一个类型转换器到管理器中，该注册会忽略转换器自身提供的源类型和转换到的目标类型。
   *
   * @param sourceType 转换的源类型的 {@link Class}。
   * @param targetType 需要转换到的类型的 {@link Class}。
   * @param typeConverter 需要注册的类型转换器实例。
   * @throws IllegalArgumentException 如果 sourceType 或者 targetType 或者 typeConverter 为 null。
   */
  <S, T> void register(
      Class<? extends S> sourceType, Class<T> targetType, TypeConverter<? extends T> typeConverter);

  /**
   * 注册一个类型转换器，源类型和转换到的目标类型由转换器提供。
   *
   * @param typeConverter 需要注册的类型转换器实例。
   * @throws IllegalArgumentException 如果 typeConverter 为 null 或者提供的源类型或者转换到的目标类型为 null 。
   */
  void register(TypeConverter<?> typeConverter);

  /**
   * 以源类型的 {@link Class}和需要转换到的类型的 {@link Class}为 key 解除一个类型转换器的注册。
   *
   * @param sourceType 需要解除注册的转换的源类型的 {@link Class}。
   * @param targetType 需要解除注册的转换到的类型的 {@link Class}。
   * @return 解除了绑定的类型转换器实例，如果解除的是一个未注册的类型转换器，则返回 null。
   */
  <S, T> TypeConverter<T> unregister(Class<S> sourceType, Class<T> targetType);

  /**
   * 以需要转换到的类型的 {@link Class}为 key 解除对应类型转换器的所有注册。
   *
   * @param targetType 需要解除注册的转换到的类型的 {@link Class}。
   * @return 解除了绑定的类型转换器实例列表，如果解除的是一个未注册的类型转换器，则返回 {@link Collections#emptyList()} 。
   */
  <T> Collection<TypeConverter<T>> unregister(Class<T> targetType);

  /**
   * 查看一个需要转换到的类型是否在类型转换器管理器中注册了类型转换器。
   *
   * @param targetType 需要转换到的类型的 {@link Class}。
   * @return 如果已经注册过一个或者多个转换到 targetType 类型的转换器返回 true ，否则返回 false。
   */
  boolean containsType(Class<?> targetType);

  /**
   * 查看一个需要转换到的类型是否在类型转换器管理器中注册了类型转换器。
   *
   * @param sourceType 源类型的 {@link Class}。
   * @param targetType 需要转换到的类型的 {@link Class}。
   * @return 如果已经注册过返回 true ，否则返回 false。
   */
  boolean containsType(Class<?> sourceType, Class<?> targetType);

  /**
   * 查看一个类型转换器是否在类型转换器管理器中被注册过。
   *
   * @param typeConverterClass 要查看的类型转换器的 {@link Class}。
   * @return 如果类型转换器管理器中至少有一个 typeConverter 的实例则返回 true。
   */
  boolean containsConverter(Class<TypeConverter<?>> typeConverterClass);

  /**
   * 判定该类型转换器管理器是否支持一组类型的转换。
   *
   * @param sourceType 源类型。
   * @param targetType 需要转换到的类型。
   * @return 如果支持 sourceType 到 targetType 的转换返回 true 。
   */
  boolean canConvert(Class<?> sourceType, Class<?> targetType);

  /**
   * 转换一个对象到目标类型。
   *
   * @param source 需要转换的值。
   * @param targetType 转换的目标值。
   * @return 转换的结果。
   * @throws ConverterNotFoundException 如果没有找到可完成 source 到 targetType 的转换器。
   * @throws TypeConversionException 如果转换发送错误。
   */
  <T> T convert(Object source, Class<T> targetType);

  static final class AllSoueceType {}
}
