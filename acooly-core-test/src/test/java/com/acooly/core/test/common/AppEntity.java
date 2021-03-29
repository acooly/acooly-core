/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-29 00:33
 */
package com.acooly.core.test.common;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangpu
 * @date 2021-03-29 00:33
 */
@Getter
@Setter
public class AppEntity extends AbstractEntity {

    private String username;
    private String name;
    private Gender gender;
    private Date Birthday;
    private Integer age;
    private Money salary;
    private SimpleStatus status;

}
