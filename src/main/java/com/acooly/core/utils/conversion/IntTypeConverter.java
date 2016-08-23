package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * int 的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class IntTypeConverter extends AbstractTypeConverter<Integer> {

	public Class<Integer> getTargetType() {
		return Integer.TYPE;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence.class);
		classes.add(CharSequence[].class);
		return classes;
	}

	public Integer convert(Object value, Class<? extends Integer> toType) {
		try {
			return intValue(value);
		} catch (NumberFormatException e) {
			throw new TypeConversionException(e);
		}
	}

	public static int intValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class) {
			return ((Number) value).intValue();
		}
		if (c == Boolean.class) {
			return ((Boolean) value).booleanValue() ? 1 : 0;
		}
		if (c == Character.class) {
			return ((Character) value).charValue();
		}
		return Integer.parseInt(StringTypeConverter.stringValue(value, true));
	}
}
