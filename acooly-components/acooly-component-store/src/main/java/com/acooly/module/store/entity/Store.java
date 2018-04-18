/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.entity;


import com.acooly.core.common.domain.AbstractEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 仓库信息 Entity
 *
 * @author zhangpu
 * Date: 2017-08-21 01:56:34
 */
@Entity
@Table(name = "st_store")
public class Store extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 仓库编码
     */
    @NotEmpty
    @Size(max = 32)
    private String storeCode;

    /**
     * 仓库名称
     */
    @NotEmpty
    @Size(max = 32)
    private String storeName;


    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    public String getStoreCode() {
        return this.storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
