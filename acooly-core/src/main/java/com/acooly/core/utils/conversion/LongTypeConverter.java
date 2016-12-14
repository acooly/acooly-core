package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * {@link Long}的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class LongTypeConverter extends AbstractTypeConverter<Long> {

	public Class<Long> getTargetType() {
		return Long.class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence.class);
		classes.add(CharSequence[].class);
		return classes;
	}

	public Long convert(Object value, Class<? extends Long> toType) {
		try {
			if (value == null) {
				return null;
			}
			return Long.valueOf(LongPrimitiveTypeConverter.longValue(value));
		} catch (Exception e) {
			throw new TypeConversionException(e);
		}
	}
}
