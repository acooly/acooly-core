/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-18 18:13 创建
 */
package com.acooly.module.filterchain;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * 上下文对象
 *
 * @author qiubo@yiji.com
 */
public abstract class Context implements Serializable {
    public transient Iterator<Filter<? extends Context>> iterator;

    protected Map<String, Object> params = Maps.newHashMap();

    /**
     * 设置此对象可以重入filterChain
     */
    public void reentry() {
        this.iterator = null;
    }

    public Object put(String key, Object value) {
        return this.params.put(key, value);
    }

    public void putAll(Map<? extends String, ?> m) {
        this.params.putAll(m);
    }

    public Object get(Object key) {
        return this.params.get(key);
    }
}
