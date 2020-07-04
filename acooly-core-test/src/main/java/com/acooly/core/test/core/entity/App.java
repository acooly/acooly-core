/*
* acooly.cn Inc.
* Copyright (c) 2020 All Rights Reserved.
* create by acooly
* date:2020-07-03
*/
package com.acooly.core.test.core.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.acooly.core.utils.Money;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
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

}
