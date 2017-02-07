/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangpu
 */
public interface Parameterize<K, V> {

	void setParameter(K key, V value);

	V getParameter(K key);

	void removeParameter(K key);

	Map<K, V> getParameters();

	void setParameters(Map<K, V> parameters);

	Set<K> keySet();

	Collection<V> values();

	void clear();

}
