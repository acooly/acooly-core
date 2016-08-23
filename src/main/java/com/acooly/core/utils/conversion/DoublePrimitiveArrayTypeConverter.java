package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * double数组的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DoublePrimitiveArrayTypeConverter extends ArrayTypeConverterSupport<double[]> {

	public DoublePrimitiveArrayTypeConverter(TypeConverterManager typeConverterManager) {
		super(typeConverterManager);
	}

	public Class<double[]> getTargetType() {
		return double[].class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence[].class);
		return classes;
	}

}
