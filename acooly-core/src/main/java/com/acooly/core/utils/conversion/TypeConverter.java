package com.acooly.core.utils.conversion;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

/**
 * 类型转换器，可通过实现该接口完成特定对象的类型的转换。该接口的实现通常被设计成单例的并且交由 {@link TypeConverterManager}
 * 管理。
 * 
 * @param <T>
 *            转换到的结果类型
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * @see TypeConverterManager
 */
public interface TypeConverter<T> {

	/**
	 * 得到转换的目标类型（目标类型）。
	 * 
	 * @return 转换的目标类型。
	 */
	Class<T> getTargetType();

	/**
	 * 得到支持转换的类型列表（源类型列表）。
	 * 
	 * @return 支持转换的类型列表。
	 */
	List<Class<?>> getSupportedSourceTypes();

	/**
	 * 将给定的值转换为给定的类型。
	 * 
	 * @param parameterMap
	 *            在转换过程中需要传递的参数。
	 * @param m
	 *            在转换过程中需要传入的构造方法、方法或者字段。
	 * @param value
	 *            需要转换的对象。
	 * @param toType
	 *            需要转换到对象的类型。
	 * @return 转换后的value。
	 * @throws TypeConversionException
	 *             转换发生错误时。
	 */
	<M extends AccessibleObject & Member> T convert(Map<? extends Object, ? extends Object> parameterMap, M m,
			Object value, Class<? extends T> toType);

	/**
	 * 将给定的值转换为给定的类型。
	 * 
	 * @param parameterMap
	 *            在转换过程中需要传递的参数。
	 * @param value
	 *            需要转换的对象。
	 * @param toType
	 *            需要转换到对象的类型。
	 * @return 转换后的value。
	 * @throws TypeConversionException
	 *             转换发生错误时。
	 * @see #convert(Object, Class)
	 */
	T convert(Map<? extends Object, ? extends Object> parameterMap, Object value, Class<? extends T> toType);

	/**
	 * 将给定的值转换为给定的类型。
	 * 
	 * @param value
	 *            需要转换的对象。
	 * @param toType
	 *            需要转换到对象的类型。
	 * @return 转换后的value。
	 * @throws TypeConversionException
	 *             转换发生错误时。
	 */
	T convert(Object value, Class<? extends T> toType);
}
