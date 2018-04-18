package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * float 的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class FloatPrimitiveTypeConverter extends AbstractTypeConverter<Float> {

    public static float floatcharValue(Object value) throws NumberFormatException {
        return (float) DoublePrimitiveTypeConverter.doubleValue(value);
    }

    public Class<Float> getTargetType() {
        return Float.TYPE;
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
            return floatcharValue(value);
        } catch (NumberFormatException e) {
            throw new TypeConversionException(e);
        }
    }
}
