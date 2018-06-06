package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Assert;
import com.acooly.core.utils.collection.ConcurrentReferenceMap;
import com.acooly.core.utils.collection.SetFromMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link TypeConverterManager} 的简单实现。
 *
 * <p>该实现分别支持源类型父类查找和目标类型的子类查找。
 *
 * <p><strong>注意：</strong>虽然对注册的转换器保证了强一致的并发性，但是由于在对源类型父类查找和目标类型的子类查找的支持时，
 * 对源类型父类和目标类型子类查找到的转换器该管理器采用了缓存的机制并且没有串行化， 所以该 <strong>缓存在极端并发条件下可能出现不一致的情况</strong>。不过从使用场景上看，
 * {@link #register(Class, Class, TypeConverter)} 、 {@link #unregister(Class)} 和 {@link
 * #unregister(Class, Class)} 这些会影响缓存的方法并不容易出现极端的并发调用，所以为了性能考虑，该实现允许这种缓存不一致的情况。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class SimpleTypeConverterManager implements TypeConverterManager {

    private final TargetTypeConverterMap typeConverterMap = new TargetTypeConverterMap(32);

    public SimpleTypeConverterManager() {
    }

    /**
     * 注册 {@link com.yjf.common.util.conversion} 包下的 {@link TypeConverter} 到 converterManager 中。
     *
     * <p>注册清单为：
     *
     * <pre>
     * converterManager.register(new ArrayTypeConverter(converterManager));
     * converterManager.register(new BytePrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new CharArrayTypeConverter(converterManager));
     * converterManager.register(new ShortPrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new IntArrayTypeConverter(converterManager));
     * converterManager.register(new LongPrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new FloatPrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new DoublePrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new BooleanPrimitiveArrayTypeConverter(converterManager));
     * converterManager.register(new StringTypeConverter());
     * converterManager.register(new BytePrimitiveTypeConverter());
     * converterManager.register(new ByteTypeConverter());
     * converterManager.register(new ShortPrimitiveTypeConverter());
     * converterManager.register(new ShortTypeConverter());
     * converterManager.register(new CharTypeConverter());
     * converterManager.register(new CharacterTypeConverter());
     * converterManager.register(new IntTypeConverter());
     * converterManager.register(new IntegerTypeConverter());
     * converterManager.register(new LongPrimitiveTypeConverter());
     * converterManager.register(new LongTypeConverter());
     * converterManager.register(new FloatPrimitiveTypeConverter());
     * converterManager.register(new FloatTypeConverter());
     * converterManager.register(new DoublePrimitiveTypeConverter());
     * converterManager.register(new DoubleTypeConverter());
     * converterManager.register(new BigIntegerTypeConverter());
     * converterManager.register(new BigDecimalTypeConverter());
     * converterManager.register(new BooleanPrimitiveTypeConverter());
     * converterManager.register(new BooleanTypeConverter());
     * converterManager.register(new ListTypeConverter());
     * converterManager.register(new SetTypeConverter());
     * converterManager.register(new QueueTypeConverter());
     * converterManager.register(new StackTypeConverter());
     * converterManager.register(new DateTypeConverter());
     * converterManager.register(new TimestampTypeConverter());
     * converterManager.register(new EnumTypeConverter());
     * </pre>
     *
     * @param converterManager 需要注册默认转换器的 TypeConverterManager 。
     */
    public static void registerDefaultConverter(TypeConverterManager converterManager) {
        if (converterManager == null) {
            return;
        }
        converterManager.register(new ArrayTypeConverter(converterManager));
        converterManager.register(new BytePrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new CharArrayTypeConverter(converterManager));
        converterManager.register(new ShortPrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new IntArrayTypeConverter(converterManager));
        converterManager.register(new LongPrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new FloatPrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new DoublePrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new BooleanPrimitiveArrayTypeConverter(converterManager));
        converterManager.register(new StringTypeConverter());
        converterManager.register(new BytePrimitiveTypeConverter());
        converterManager.register(new ByteTypeConverter());
        converterManager.register(new ShortPrimitiveTypeConverter());
        converterManager.register(new ShortTypeConverter());
        converterManager.register(new CharTypeConverter());
        converterManager.register(new CharacterTypeConverter());
        converterManager.register(new IntTypeConverter());
        converterManager.register(new IntegerTypeConverter());
        converterManager.register(new LongPrimitiveTypeConverter());
        converterManager.register(new LongTypeConverter());
        converterManager.register(new FloatPrimitiveTypeConverter());
        converterManager.register(new FloatTypeConverter());
        converterManager.register(new DoublePrimitiveTypeConverter());
        converterManager.register(new DoubleTypeConverter());
        converterManager.register(new BigIntegerTypeConverter());
        converterManager.register(new BigDecimalTypeConverter());
        converterManager.register(new BooleanPrimitiveTypeConverter());
        converterManager.register(new BooleanTypeConverter());
        converterManager.register(new ListTypeConverter());
        converterManager.register(new SetTypeConverter());
        converterManager.register(new QueueTypeConverter());
        converterManager.register(new DateTypeConverter());
        converterManager.register(new TimestampTypeConverter());
        converterManager.register(new EnumTypeConverter());
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<TypeConverter<T>> unregister(Class<T> targetType) {
        if (targetType == null) {
            return Collections.emptyList();
        }
        SourceTypeConverterMap removed = this.typeConverterMap.remove(targetType);
        return (Collection<TypeConverter<T>>)
                (removed == null ? Collections.emptyList() : removed.values());
    }

    @SuppressWarnings("unchecked")
    public <S, T> TypeConverter<T> unregister(Class<S> sourceType, Class<T> targetType) {
        if (sourceType == null || targetType == null) {
            return null;
        }
        SourceTypeConverterMap removed = getSourceTypeConverterMap(targetType);
        if (removed == null) {
            return null;
        }
        TypeConverter<?> removedTypeConverter = removed.remove(sourceType);
        return (TypeConverter<T>) removedTypeConverter;
    }

    public <S, T> void register(
            Class<? extends S> sourceType,
            Class<T> targetType,
            TypeConverter<? extends T> typeConverter) {
        Assert.notNull(typeConverter, "{typeConverter} 不能为 'null' 。");
        doRegister(sourceType, targetType, typeConverter);
    }

    public void register(TypeConverter<?> typeConverter) {
        Assert.notNull(typeConverter, "{typeConverter} 不能为 'null' 。");
        List<Class<?>> supportedSourceTypes = typeConverter.getSupportedSourceTypes();
        Assert.notEmpty(supportedSourceTypes, "{typeConverter} 提供的源类型列表为空。");
        for (Class<?> supportedSourceType : supportedSourceTypes) {
            doRegister(supportedSourceType, typeConverter.getTargetType(), typeConverter);
        }
    }

    private void doRegister(
            Class<?> sourceType, Class<?> targetType, TypeConverter<?> typeConverter) {
        Assert.notNull(sourceType, "{sourceType} 不能为 'null' 。");
        Assert.notNull(targetType, "{targetType} 不能为 'null' 。");
        SourceTypeConverterMap sourceTypeMap = this.typeConverterMap.get(targetType);
        if (sourceTypeMap == null) {
            sourceTypeMap = new SourceTypeConverterMap(32);
            SourceTypeConverterMap old = this.typeConverterMap.putIfAbsent(targetType, sourceTypeMap);
            if (old != null) {
                sourceTypeMap = old;
            } else {
                this.typeConverterMap.clearAllNotTargetTypeConverter();
            }
        }
        sourceTypeMap.put(sourceType, typeConverter);
        sourceTypeMap.putCache(targetType, typeConverter);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<TypeConverter<T>> getTypeConverter(Class<T> targetType) {
        if (targetType == null) {
            return Collections.emptyList();
        }
        ConcurrentMap<Class<?>, TypeConverter<?>> m = getSourceTypeConverterMap(targetType);
        return (Collection<TypeConverter<T>>)
                (m == null ? Collections.emptyList() : Collections.unmodifiableCollection(m.values()));
    }

    @SuppressWarnings("unchecked")
    public <S, T> TypeConverter<T> getTypeConverter(Class<S> sourceType, Class<T> targetType) {
        if (sourceType == null || targetType == null) {
            return null;
        }
        SourceTypeConverterMap m = getSourceTypeConverterMap(targetType);
        if (m == null) {
            return null;
        }
        TypeConverter<?> typeConverter = m.get(sourceType);
        if (typeConverter == null) {
            if (!m.notSourceTypeConverter(sourceType)) {
                typeConverter = m.getCache(sourceType);
            }
            if (typeConverter == null) {
                typeConverter = m.get(TypeConverterManager.ALL_SOUECE_TYPE_CLASS);
                if (typeConverter != null) {
                    m.putCache(TypeConverterManager.ALL_SOUECE_TYPE_CLASS, typeConverter);
                }
            }
            if (typeConverter == null) {
                for (Entry<Class<?>, TypeConverter<?>> entry : m.entrySet()) {
                    if (entry.getKey().isAssignableFrom(sourceType)) {
                        typeConverter = entry.getValue();
                        m.putCache(sourceType, typeConverter);
                        return (TypeConverter<T>) typeConverter;
                    }
                }
            }
            if (typeConverter == null) {
                m.addNotSourceTypeConverter(sourceType);
            }
        }
        return (TypeConverter<T>) typeConverter;
    }

    private <T> SourceTypeConverterMap getSourceTypeConverterMap(Class<T> targetType) {
        SourceTypeConverterMap m = this.typeConverterMap.get(targetType);
        if (m == null && !this.typeConverterMap.notTargetTypeConverter(targetType)) {
            m = this.typeConverterMap.getCache(targetType);
            if (m == null) {
                for (Entry<Class<?>, SourceTypeConverterMap> entry : this.typeConverterMap.entrySet()) {
                    if (targetType.isAssignableFrom(entry.getKey())) {
                        m = entry.getValue();
                        this.typeConverterMap.putCache(targetType, m);
                        break;
                    }
                }
            }
            if (m == null) {
                this.typeConverterMap.addNotTargetTypeConverter(targetType);
            }
        }
        return m;
    }

    public boolean containsType(Class<?> targetType) {
        if (targetType == null) {
            return false;
        }
        return getTypeConverter(targetType) != null;
    }

    public boolean containsType(Class<?> sourceType, Class<?> targetType) {
        if (sourceType == null || targetType == null) {
            return false;
        }
        return getTypeConverter(sourceType, targetType) != null;
    }

    public boolean containsConverter(Class<TypeConverter<?>> typeConverterClass) {
        if (typeConverterClass == null) {
            return false;
        }
        Collection<SourceTypeConverterMap> values = this.typeConverterMap.values();
        for (ConcurrentMap<Class<?>, TypeConverter<?>> typeConverterMap : values) {
            for (TypeConverter<?> typeConverter : typeConverterMap.values()) {
                if (typeConverterClass == typeConverter.getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getTypeConverter(sourceType, targetType) != null;
    }

    public <T> T convert(Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        TypeConverter<T> typeConverter = getTypeConverter(source.getClass(), targetType);
        if (typeConverter == null) {
            throw new ConverterNotFoundException(source.getClass(), targetType);
        }
        return typeConverter.convert(source, targetType);
    }

    static class SourceTypeConverterMap extends ConcurrentReferenceMap<Class<?>, TypeConverter<?>> {

        /**
         * 版本号
         */
        private static final long serialVersionUID = 5918105874756497516L;

        private final ConcurrentMap<Class<?>, TypeConverter<?>> sourceTypeConverterCache =
                new ConcurrentReferenceMap<Class<?>, TypeConverter<?>>(
                        ReferenceKeyType.WEAK, ReferenceValueType.STRONG, 32);

        private final Set<Class<?>> notSourceTypeConverters =
                new SetFromMap<Class<?>, Object>(
                        new ConcurrentReferenceMap<Class<?>, Object>(
                                ReferenceKeyType.WEAK, ReferenceValueType.STRONG, 32));

        public SourceTypeConverterMap(int initialCapacity) {
            super(ReferenceKeyType.WEAK, ReferenceValueType.STRONG, initialCapacity);
        }

        public TypeConverter<?> remove(Object key) {
            TypeConverter<?> removedTypeConverter = super.remove(key);
            if (removedTypeConverter != null) {
                Class<?> sourceType = (Class<?>) key;
                for (Iterator<Class<?>> iterator = this.sourceTypeConverterCache.keySet().iterator();
                     iterator.hasNext();
                        ) {
                    if (sourceType.isAssignableFrom(iterator.next())) {
                        iterator.remove();
                    }
                }
            }
            return removedTypeConverter;
        }

        @Override
        public void clear() {
            this.sourceTypeConverterCache.clear();
            super.clear();
        }

        public boolean notSourceTypeConverter(Class<?> sourceType) {
            return this.notSourceTypeConverters.contains(sourceType);
        }

        public void addNotSourceTypeConverter(Class<?> sourceType) {
            this.notSourceTypeConverters.add(sourceType);
        }

        public TypeConverter<?> getCache(Class<?> sourceType) {
            return this.sourceTypeConverterCache.get(sourceType);
        }

        public void putCache(Class<?> sourceType, TypeConverter<?> typeConverter) {
            this.sourceTypeConverterCache.put(sourceType, typeConverter);
        }
    }

    static class TargetTypeConverterMap
            extends ConcurrentReferenceMap<Class<?>, SourceTypeConverterMap> {

        /**
         * 版本号
         */
        private static final long serialVersionUID = 4321717941721294620L;

        private final ConcurrentMap<Class<?>, SourceTypeConverterMap> targetTypeConverterCacheMap =
                new ConcurrentReferenceMap<Class<?>, SourceTypeConverterMap>(
                        ReferenceKeyType.WEAK, ReferenceValueType.STRONG, 32);

        private final Set<Class<?>> notTargetTypeConverters =
                new SetFromMap<Class<?>, Object>(
                        new ConcurrentReferenceMap<Class<?>, Object>(
                                ReferenceKeyType.WEAK, ReferenceValueType.STRONG, 32));

        public TargetTypeConverterMap(int initialCapacity) {
            super(ReferenceKeyType.WEAK, ReferenceValueType.STRONG, initialCapacity);
        }

        @Override
        public SourceTypeConverterMap remove(Object key) {
            SourceTypeConverterMap removed = super.remove(key);
            if (removed != null) {
                Class<?> targetType = (Class<?>) key;
                for (Iterator<Class<?>> iterator = this.targetTypeConverterCacheMap.keySet().iterator();
                     iterator.hasNext();
                        ) {
                    if (iterator.next().isAssignableFrom(targetType)) {
                        iterator.remove();
                    }
                }
            }
            return removed;
        }

        public boolean notTargetTypeConverter(Class<?> targetType) {
            return this.notTargetTypeConverters.contains(targetType);
        }

        public void addNotTargetTypeConverter(Class<?> targetType) {
            this.notTargetTypeConverters.add(targetType);
        }

        public void clearAllNotTargetTypeConverter() {
            this.notTargetTypeConverters.clear();
        }

        public void putCache(Class<?> targetType, SourceTypeConverterMap sourceTypeConverterMap) {
            this.targetTypeConverterCacheMap.put(targetType, sourceTypeConverterMap);
        }

        public SourceTypeConverterMap getCache(Class<?> targetType) {
            return this.targetTypeConverterCacheMap.get(targetType);
        }
    }
}
