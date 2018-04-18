package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * {@link Byte}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class ByteTypeConverter extends AbstractTypeConverter<Byte> {

    public Class<Byte> getTargetType() {
        return Byte.class;
    }

    public List<Class<?>> getSupportedSourceTypes() {
        List<Class<?>> classes = super.getSupportedSourceTypes();
        classes.add(Object[].class);
        classes.add(Collection.class);
        classes.add(CharSequence.class);
        classes.add(CharSequence[].class);
        return classes;
    }

    public Byte convert(Object value, Class<? extends Byte> toType) {
        try {
            if (value == null) {
                return null;
            }
            return Byte.valueOf(BytePrimitiveTypeConverter.byteValue(value));
        } catch (Exception e) {
            throw new TypeConversionException(e);
        }
    }
}
