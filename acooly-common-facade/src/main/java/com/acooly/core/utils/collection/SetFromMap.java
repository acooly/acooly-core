/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.core.utils.collection;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * 创建一个 {@link Map} 的 key 的 {@link Set} 包装。对该包装的任何修改都会反应到被包装的 {@link Map}。
 *
 * <p>该 {@link Set} 的特性与被包装的 {@link Map#keySet()} 相同但是支持 add 操作（除非被包装的 {@link Map#put(Object,
 * Object)} 本身不可用），底层实现是调用 {@link Map#put(Object, Object)} 。对于 value ，会使用 {@link #defaultV}。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class SetFromMap<E, V> extends AbstractSet<E> implements Set<E>, Serializable {

    /**
     * 使用 {@link #SetFromMap(Map)} 构造 {@link SetFromMap} 时，调用 {@link #add(Object)} 时的默认 value
     */
    protected static final Object DEFAULT_V = new Object();
    /**
     * 版本号
     */
    private static final long serialVersionUID = 6498728436527071738L;
    /**
     * 指定的调用 {@link #add(Object)} 时的默认 value
     */
    protected V defaultV;

    /**
     * 被包装的 {@link Map}
     */
    protected Map<E, V> map;

    /**
     * 被包装 {@link Map} 的 {@link Map#keySet()}
     */
    protected transient Set<E> set;

    /**
     * 创建一个 SetFromMap ，在调用 {@link #add(Object)} 时会使用 {@link #DEFAULT_V} 作为被包装 {@link Map} 的 value 。
     *
     * @param map 被包装的 {@link Map}。
     * @throws NullPointerException 如果 map 为 null。
     */
    @SuppressWarnings("unchecked")
    public SetFromMap(Map<E, Object> map) {
        this.map = (Map<E, V>) map;
        this.set = map.keySet();
        this.defaultV = (V) DEFAULT_V;
    }

    /**
     * 创建一个 SetFromMap ，在调用 {@link #add(Object)} 时会使用 defaultV 指定的值作为被包装 {@link Map} 的 value 。
     *
     * @param map      被包装的 {@link Map}。
     * @param defaultV 在调用 {@link #add(Object)} 时所使用作为被包装 {@link Map} 的 value 。
     * @throws NullPointerException 如果 map 为 null。
     */
    public SetFromMap(Map<E, V> map, V defaultV) {
        this.map = (Map<E, V>) map;
        this.set = map.keySet();
        this.defaultV = defaultV;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        return this.map.remove(o) != null;
    }

    @Override
    public boolean add(E e) {
        return this.map.put(e, this.defaultV) == null;
    }

    /**
     * 包装调用 {@link Map#put(Object, Object)}。
     *
     * @param e     与指定值相关联的键。
     * @param value 与指定键相关联的值。
     * @return 以前与指定键相关联的值。
     * @see Map#put(Object, Object)
     */
    public V put(E e, V value) {
        return this.map.put(e, value);
    }

    public Iterator<E> iterator() {
        return this.set.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.set.toArray(a);
    }

    @Override
    public String toString() {
        return this.set.toString();
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || this.set.equals(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.set.retainAll(c);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.set = this.map.keySet();
    }
}
