/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-03-01
*/
package com.acooly.module.portlet.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.portlet.enums.PortletConfigTypeEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * p_portlet_config Entity
 *
 * @author acooly
 *         Date: 2017-03-01 00:53:18
 */
@Entity
@Table(name = "p_portlet_config")
public class PortletConfig extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    private PortletConfigTypeEnum type;
    /**
     * 标题
     */
    private String title;
    /**
     * 参数键
     */
    private String key;
    /**
     * 参数值
     */
    private String value;
    /**
     * 备注
     */
    private String comments;
    /**
     * create_time
     */
    private Date createTime;
    /**
     * update_time
     */
    private Date updateTime;


    public PortletConfigTypeEnum getType() {
        return this.type;
    }

    public void setType(PortletConfigTypeEnum type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
