/*
 * acooly.cn Inc.
 * Copyright (c) 2022 All Rights Reserved.
 * create by acooly
 * date:2022-06-25
 */
package com.acooly.core.test.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.core.utils.ie.ExportColumn;
import com.acooly.core.utils.ie.ExportModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 代码生成客户信息 Entity
 *
 * @author acooly
 * @date 2022-06-25 23:15:10
 */
@Entity
@Table(name = "acooly_coder_customer")
@Getter
@Setter
// 导出规则：
// 1、@ExportModel标记的实体
// 2、@ExportColumn标记的列
@ExportModel(fileName = "客户信息",
        // 父类属性的导出配置
        exportColumns = {
                @ExportColumn(name = "id", title = "ID", order = 1),
                @ExportColumn(name = "createTime", title = "创建时间", order = 18),
        },
        // 父类忽略的属性
        ignores = {"updateTime"})
public class CoderCustomer extends AbstractEntity {

    /**
     * 用户名
     */
    @NotBlank
    @Size(max = 32)
    @ExportColumn(title = "用户名", order = 3)
    private String username;

    /**
     * 年龄
     */
    @ExportColumn(title = "年龄", order = 4)
    private Integer age;

    /**
     * 生日
     */
    @NotNull
    @ExportColumn(title = "生日", order = 5)
    private Date birthday;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @ExportColumn(title = "性别", order = 6)
    private Gender gender;

    /**
     * 生肖
     */
    @Enumerated(EnumType.STRING)
    @ExportColumn(title = "生肖", order = 7)
    private AnimalSign animal;


    /**
     * 手机号码
     * 请手机号码是自有使用，以确保后续所有业务通知您能收到。
     */
    @Size(max = 11)
    @ExportColumn(title = "手机号码", order = 8)
    private String mobileNo;

    /**
     * 邮件
     */
    @Size(max = 64)
    @ExportColumn(title = "邮件", order = 9)
    private String mail;


    /**
     * 详情
     */
    @ExportColumn(title = "详情", order = 10)
    private String content;

    /**
     * 完成度
     * 任务完成度，根据不同完成度获得对应的特权。<li>1、50%以下：基本会员权限</li><li>1、50%以上：VIP会员权限</li>
     */
    @ExportColumn(title = "完成度", order = 11)
    private Integer doneRatio;

    /**
     * 付款率
     * 演示说明：付款率字段采用支持2位小数的百分数（15.55%）。注意点如下: <li>1、数据库字段类型采用BIGINT</li><li>2、实体类型采用Money</li><li>3、数据库保存的是万分位值（例如:1555表示15.55%）</li>
     */
    @ExportColumn(title = "付款率", order = 12)
    private Money payRate;

    /**
     * 薪水
     */
    @ExportColumn(title = "薪水", order = 13)
    private Money salary;

    /**
     * 注册渠道
     * alias属性演示：采用内置channel别名对应的枚举`ChannelEnum`生成下拉列表
     */
    @Enumerated(EnumType.STRING)
    @ExportColumn(title = "注册渠道", order = 14)
    private ChannelEnum registryChannel;

    /**
     * 推送广告
     */
    @Enumerated(EnumType.STRING)
    private WhetherStatus pushAdv;

    /**
     * 数字类型
     */
    private Integer numStatus;

    /**
     * 网址
     */
    @Size(max = 128)
    @ExportColumn(title = "网址", order = 20)
    private String website;

    /**
     * 照片
     */
    @Size(max = 128)
    @ExportColumn(title = "照片", order = 21)
    private String photoPath;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SimpleStatus status;

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

}
