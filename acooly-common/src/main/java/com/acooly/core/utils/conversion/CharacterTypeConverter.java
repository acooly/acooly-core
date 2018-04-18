package com.acooly.core.utils.conversion;

import java.util.Collection;
import java.util.List;

/**
 * {@link Character}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class CharacterTypeConverter extends AbstractTypeConverter<Character> {

    public Class<Character> getTargetType() {
        return Character.class;
    }

    public List<Class<?>> getSupportedSourceTypes() {
        List<Class<?>> classes = super.getSupportedSourceTypes();
        classes.add(Object[].class);
        classes.add(Collection.class);
        classes.add(CharSequence.class);
        classes.add(CharSequence[].class);
        return classes;
    }

    public Character convert(Object value, Class<? extends Character> toType) {
        try {
            if (value == null) {
                return null;
            }
            return Character.valueOf(CharTypeConverter.charValue(value));
        } catch (Exception e) {
            throw new TypeConversionException(e);
        }
    }
}
