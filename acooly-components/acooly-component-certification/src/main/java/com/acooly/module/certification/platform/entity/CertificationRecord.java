/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-04-21
*/
package com.acooly.module.certification.platform.entity;


import com.acooly.core.common.domain.AbstractEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 实名认证记录表 Entity
 *
 * @author acooly
 *         Date: 2017-04-21 16:50:19
 */
@Entity
@Table(name = "cert_certification_record")
public class CertificationRecord extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 真实姓名
     */
    @Size(max = 32)
    private String realName;

    /**
     * 身份证号
     */
    @Size(max = 18)
    private String idCarNo;

    /**
     * 性别
     */
    @NotEmpty
    @Size(max = 18)
    private String sex;

    /**
     * 所在地址
     */
    @NotEmpty
    @Size(max = 512)
    private String address;

    /**
     * 出生日期
     */
    @NotEmpty
    @Size(max = 32)
    private String birthday;

    /**
     * 状态
     */
    @NotNull
    private Integer status = 0;


    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCarNo() {
        return this.idCarNo;
    }

    public void setIdCarNo(String idCarNo) {
        this.idCarNo = idCarNo;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
