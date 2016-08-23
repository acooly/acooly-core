package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * {@link Boolean}的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class BooleanTypeConverter extends AbstractTypeConverter<Boolean> {

	public Class<Boolean> getTargetType() {
		return Boolean.class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence.class);
		classes.add(CharSequence[].class);
		return classes;
	}

	public Boolean convert(Object value, Class<? extends Boolean> toType) {
		if (value == null) {
			return null;
		}
		return BooleanPrimitiveTypeConverter.booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
	}
}
