package com.acooly.core.utils.conversion;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * {@link String}的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class StringTypeConverter extends AbstractTypeConverter<String> {

	public Class<String> getTargetType() {
		return String.class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		return Arrays.<Class<?>> asList(TypeConverterManager.ALL_SOUECE_TYPE_CLASS);
	}

	public String convert(Object value, Class<? extends String> toType) {
		return stringValue(value);
	}

	public static String stringValue(Object value, boolean trim) {
		String result;
		if (value == null) {
			result = null;
		} else {
			if (value.getClass().isArray()) {
				StringBuilder builder = new StringBuilder();
				if (value instanceof Object[]) {
					for (Object o : (Object[]) value) {
						builder.append(o);
					}
				} else {
					int length = Array.getLength(value);
					for (int i = 0; i < length; i++) {
						builder.append(Array.get(value, i));
					}
				}
				result = builder.toString();
			} else {
				result = value.toString();
			}
			if (trim) {
				result = result.trim();
			}
		}
		return result;
	}

	public static String stringValue(Object value) {
		return stringValue(value, false);
	}
}
