package com.acooly.module.portlet.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.portlet.enums.FeedbackType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 客户反馈 Entity
 * <p>
 * Date: 2015-05-19 21:58:49
 *
 * @author Acooly Code Generator
 */
@Table(name = "portlet_feedback")
@Getter
@Setter
public class Feedback extends AbstractEntity {


    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    private FeedbackType type = FeedbackType.suggest;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 联系信息
     */
    private String contactInfo;
    /**
     * 备注
     */
    private String comments;
}
