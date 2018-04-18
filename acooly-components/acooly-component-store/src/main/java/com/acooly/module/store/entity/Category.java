/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.entity;


import com.acooly.core.common.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 品类信息 Entity
 *
 * @author zhangpu
 * Date: 2017-08-21 01:56:35
 */
@Entity
@Table(name = "st_category")
public class Category extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 查询路径
     */
    @Size(max = 64)
    private String path;

    /**
     * 仓库编码
     */
    @Size(max = 32)
    private String storeCode;

    /**
     * 品类名称
     */
    @Size(max = 32)
    private String name;


    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStoreCode() {
        return this.storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
