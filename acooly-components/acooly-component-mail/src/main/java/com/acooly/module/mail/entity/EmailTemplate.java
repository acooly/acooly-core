/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-04-27
*/
package com.acooly.module.mail.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.NotNull;

import com.acooly.core.common.domain.AbstractEntity;

import java.util.Date;

/**
 * @author shuijing
 */
@Getter
@Setter
@Entity
@Table(name = "email_template")
public class EmailTemplate extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 模版名称
     */
    @NotEmpty
    private String name;

    /**
     * 模板邮件主题
     */
    private String subject;

    /**
     * 模板邮件内容
     */
    private String content;

}