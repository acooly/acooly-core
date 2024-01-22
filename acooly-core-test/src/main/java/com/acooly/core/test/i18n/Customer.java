/*
 * acooly.cn Inc.
 * Copyright (c) 2022 All Rights Reserved.
 * create by acooly
 * date:2022-06-25
 */
package com.acooly.core.test.i18n;


import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.facade.InfoBase;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.validate.jsr303.CertNo;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.core.utils.validate.jsr303.MobileNo;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * Bean Validator测试用例
 *
 * @author zhangpu
 * @date 2024-01-20
 */
@Getter
@Setter
public class Customer extends InfoBase {

    /**
     * 用户名
     */
    @NotBlank
    @Size(max = 32)
    private String username;


    /**
     * 生日
     */

    @NotNull
    private Date birthday;

    /**
     * 性别
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;


    /**
     * 手机号码
     * 请手机号码是自有使用，以确保后续所有业务通知您能收到。
     */
    @MobileNo(blankable = false)
    private String mobileNo;

    /**
     * 邮件
     */
    @Size(max = 64)
    @Email
    @NotNull
    private String mail;

    /**
     * 主页地址
     */
    @HttpUrl(blankable = false)
    private String homePage;

    /**
     * 身份证号码
     */
    @CertNo
    private String certNo;

    /**
     * 薪水
     * 不能为空
     */
    @MoneyConstraint(min = 1000, max = 1000000)
    private Money salary;

    /**
     * 金额
     * 可以为空，检查格式
     */
    @MoneyConstraint(min = 1012, max = 10001, nullable = true)
    private Money amount;


    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SimpleStatus status;


}
