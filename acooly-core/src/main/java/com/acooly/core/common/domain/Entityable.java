/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-19 16:17 创建
 */
package com.acooly.core.common.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author acooly
 */
public interface Entityable extends Serializable, Persistable<Serializable> {

    Serializable getId();

    Date getCreateTime();

    Date getUpdateTime();

    @Transient
    default boolean isNew() {
        return null == getId();
    }
}
