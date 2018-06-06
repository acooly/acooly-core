package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Reflections;

import java.util.*;

/**
 * {@link Queue}的类型转换器。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class QueueTypeConverter extends AbstractTypeConverter<Queue<?>> {

    @SuppressWarnings("unchecked")
    public static <E extends Queue<?>> E queueValue(Object value, Class<? extends E> queueClassType) {
        Queue<Object> queue = null;
        if ((Class<?>) queueClassType == Queue.class) {
            // 使用 LinkedList 作为实现
            queue = new LinkedList<Object>();
        } else { // 如果是具体的类则使用该类的类型
            try {
                queue = (Queue<Object>) Reflections.createObject(queueClassType);
            } catch (Exception e) {
                throw new TypeConversionException(e);
            }
        }
        queue.addAll((Queue<Object>) value);
        return (E) queue;
    }

    @SuppressWarnings("unchecked")
    public Class<Queue<?>> getTargetType() {
        Class<?> queueClass = Queue.class;
        return (Class<Queue<?>>) queueClass;
    }

    public List<Class<?>> getSupportedSourceTypes() {
        return Arrays.asList(Collection.class, Object[].class);
    }

    public Queue<?> convert(Object value, Class<? extends Queue<?>> toType) {
        return queueValue(value, toType);
    }
}
