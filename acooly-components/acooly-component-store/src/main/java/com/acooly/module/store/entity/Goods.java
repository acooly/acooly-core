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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 商品信息 Entity
 *
 * @author zhangpu
 * Date: 2017-08-21 01:56:35
 */
@Entity
@Table(name = "st_goods")
public class Goods extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 仓库编码
     */
    @Size(max = 32)
    private String storeCode;

    /**
     * 品类ID
     */
    @NotNull
    private Long categoryId;

    /**
     * 品类名称
     */
    @Size(max = 32)
    private String categoryName;

    /**
     * 商品编码
     */
    @Size(max = 32)
    private String code;

    /**
     * name
     */
    @Size(max = 32)
    private String name;

    /**
     * 商品简介
     */
    @Size(max = 255)
    private String descn;

    /**
     * 单价
     */
    private Long price;

    /**
     * 单位
     */
    @Size(max = 16)
    private String unit;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 型号
     */
    @Size(max = 32)
    private String model;

    /**
     * 品牌
     */
    @Size(max = 32)
    private String brand;

    /**
     * 供应商
     */
    @Size(max = 32)
    private String supplier;

    /**
     * 展示地址
     */
    @Size(max = 128)
    private String detailUrl;


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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescn() {
        return this.descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDetailUrl() {
        return this.detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
