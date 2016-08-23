package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * short 的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class ShortPrimitiveTypeConverter extends AbstractTypeConverter<Short> {

	public Class<Short> getTargetType() {
		return Short.TYPE;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence.class);
		classes.add(CharSequence[].class);
		return classes;
	}

	public Short convert(Object value, Class<? extends Short> toType) {
		try {
			return shortValue(value);
		} catch (Exception e) {
			throw new TypeConversionException(e);
		}
	}

	public static short shortValue(Object value) throws NumberFormatException {
		return (short) IntTypeConverter.intValue(value);
	}
}
