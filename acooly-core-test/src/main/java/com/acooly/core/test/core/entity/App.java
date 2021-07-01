/*
 * acooly.cn Inc.
 * Copyright (c) 2020 All Rights Reserved.
 * create by acooly
 * date:2020-07-03
 */
package com.acooly.core.test.core.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.BigMoney;
import com.acooly.core.utils.Money;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * app Entity
 *
 * @author acooly
 * @date 2020-07-03 16:28:38
 */
@Entity
@Table(name = "app")
@Getter
@Setter
public class App extends AbstractEntity {

    /**
     * display_name
     */
    @Size(max = 255)
    private String displayName;

    /**
     * name
     */
    @Size(max = 255)
    private String name;

    /**
     * parent_app_id
     */
    private Long parentAppId;

    /**
     * parent_id
     */
    private Long parentId;

    /**
     * raw_add_time
     */
    private Date rawAddTime;

    /**
     * raw_update_time
     */
    private Date rawUpdateTime;

    /**
     * type
     */
    @Size(max = 255)
    private String type;

    /**
     * user_id
     */
    private Long userId;

    /**
     * price
     */
    private Money price;

    private BigMoney amount;

}
