/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-29 00:36
 */
package com.acooly.core.test.common;

import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.facade.DtoBase;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 * @author zhangpu
 * @date 2021-03-29 00:36
 */
@Getter
@Setter
public class AppDto extends DtoBase {
    private String username;
    private Gender gender;
    private String genderText;
    private Date Birthday;
    private Integer age;
    private Money salary;
    private SimpleStatus status;
    private String statusText;
}
