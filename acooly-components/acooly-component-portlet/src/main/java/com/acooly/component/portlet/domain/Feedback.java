package com.acooly.component.portlet.domain;

import com.acooly.component.portlet.enums.FeedbackType;
import com.acooly.core.common.domain.AbstractEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * 客户反馈 Entity
 * <p>
 * Date: 2015-05-19 21:58:49
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "p_feedback")
public class Feedback extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1758979113949548569L;

    @Id
    @GeneratedValue
    protected Long id;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackType getType() {
        return type;
    }

    public void setType(FeedbackType type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
