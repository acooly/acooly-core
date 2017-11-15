/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-11-15 00:52 创建
 */
package com.acooly.module.safety.key;

import com.acooly.module.safety.exception.SafetyException;
import com.acooly.module.safety.exception.SafetyResultCode;

/**
 * Key 加载抽象类
 *
 * @author zhangpu 2017-11-15 00:52
 */
public abstract class AbstractKeyLoadManager<T> implements KeyLoader<T> {

    protected T t;

    @Override
    public T load(String principal) {
        try {
            if (t == null) {
                synchronized (this) {
                    if (t == null) {
                        t = doLoad(principal);
                    }
                }
            }
            return t;
        } catch (Exception e) {
            throw new SafetyException(SafetyResultCode.LOAD_KEYSTORE_ERROR, e.getMessage());
        }
    }

    /**
     * 实现从外部资源获取和组织Key
     *
     * @param principal
     * @return
     */
    public abstract T doLoad(String principal);


}
