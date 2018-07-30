/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-19 16:17 创建
 */
package com.acooly.core.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    void setCreateTime(Date date);

    Date getUpdateTime();

    void setUpdateTime(Date date);

    @Transient
    @JsonIgnore
    default boolean isNew() {
        return null == getId();
    }
}
