package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * byte数组的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class BytePrimitiveArrayTypeConverter extends ArrayTypeConverterSupport<byte[]> {

    public BytePrimitiveArrayTypeConverter(TypeConverterManager typeConverterManager) {
        super(typeConverterManager);
    }

    public Class<byte[]> getTargetType() {
        return byte[].class;
    }

    public List<Class<?>> getSupportedSourceTypes() {
        List<Class<?>> classes = super.getSupportedSourceTypes();
        classes.add(Object[].class);
        classes.add(Collection.class);
        classes.add(CharSequence[].class);
        return classes;
    }
}
