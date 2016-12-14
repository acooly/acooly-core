package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * {@link Float}的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class FloatTypeConverter extends AbstractTypeConverter<Float> {

	public Class<Float> getTargetType() {
		return Float.class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence.class);
		classes.add(CharSequence[].class);
		return classes;
	}

	public Float convert(Object value, Class<? extends Float> toType) {
		try {
			if (value == null) {
				return null;
			}
			return Float.valueOf(FloatPrimitiveTypeConverter.floatcharValue(value));
		} catch (NumberFormatException e) {
			throw new TypeConversionException(e);
		}
	}
}
