package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * int数组的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class IntArrayTypeConverter extends ArrayTypeConverterSupport<int[]> {

	public IntArrayTypeConverter(TypeConverterManager typeConverterManager) {
		super(typeConverterManager);
	}

	public Class<int[]> getTargetType() {
		return int[].class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		List<Class<?>> classes = super.getSupportedSourceTypes();
		classes.add(Object[].class);
		classes.add(Collection.class);
		classes.add(CharSequence[].class);
		return classes;
	}

}
