/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.core.common.facade;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * @author zhangpu
 */
public class LinkedHashMapParameterize<K, V> implements Parameterize<K, V> {

	Map<K, V> map = Maps.newLinkedHashMap();

	@Override
	public void setParameter(K key, V value) {
		this.map.put(key, value);
	}

	@Override
	public V getParameter(K key) {
		return this.map.get(key);
	}

	@Override
	public void removeParameter(K key) {
		this.map.remove(key);
	}

	@Override
	public Map<K, V> getParameters() {
		return this.map;
	}

	@Override
	public void setParameters(Map<K, V> parameters) {
		this.map = parameters;
	}

	@Override
	public Set<K> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.map.values();
	}

	@Override
	public void clear() {
		this.map.clear();
	}

}
