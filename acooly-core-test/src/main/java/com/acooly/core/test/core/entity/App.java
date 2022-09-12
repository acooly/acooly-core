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
import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
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
@ExportModel(name = "应用信息", border = true, headerShow = true)
public class App extends AbstractEntity {

    /**
     * display_name
     */
    @Size(max = 255)
    @ExportColumn(header = "显示名称", order = 3)
    private String displayName;

    /**
     * name
     */
    @Size(max = 255)
    @ExportColumn(header = "名称", order = 4)
    private String name;

    /**
     * parent_app_id
     */
    @ExportColumn(header = "父应用ID", order = 5)
    private Long parentAppId;

    /**
     * parent_id
     */
    @ExportColumn(header = "父ID", order = 6)
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
    @ExportColumn(header = "类型", order = 7)
    private String type;

    /**
     * user_id
     */
    @ExportColumn(header = "用户ID", order = 8)
    private Long userId;

    /**
     * price
     */
    @ExportColumn(header = "价格", order = 9)
    private Money price;

    @ExportColumn(header = "金额", order = 10)
    private BigMoney amount;

    @ExportColumn(header = "余额", order = 11)
    private BigMoney balance;

}
