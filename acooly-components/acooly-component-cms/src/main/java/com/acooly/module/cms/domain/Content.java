package com.acooly.module.cms.domain;

import com.acooly.core.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 内容主表 Entity
 * <p>
 * Date: 2013-07-12 15:06:46
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "CMS_CONTENT")
public class Content extends AbstractEntity {

    public static final int STATUS_ENABLED = 1;// 1:正常
    public static final int STATUS_DISABLED = 2;// 2:禁用

    /**
     * uid
     */
    private static final long serialVersionUID = 7860091097881184418L;

    /**
     * 编码 唯一
     */
    private String keycode;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 发布时间
     */
    private Date pubDate = new Date();
    /**
     * 关键字 (SEO或本身简单搜索使用)
     */
    private String keywords;
    /**
     * 主题介绍
     */
    private String subject;
    /**
     * 作者
     */
    private String author;
    /**
     * 点击数
     */
    private Long hits = 1L;
    /**
     * 状态 (1:正常,2:禁用)
     */
    private int status = STATUS_ENABLED;
    /**
     * 备注
     */
    private String comments;

    /**
     * 扩展CODE(分类维度考虑，比如：所属商户，所属用户等编码)
     **/
    private String belongCode;

    /**
     * 所属分类
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "type")
    private ContentType contentType;

    /**
     * 内容主体
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    private ContentBody contentBody;

    /**
     * 附件列表
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "content")
    private Set<Attachment> attachments = new HashSet<Attachment>();


    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }


    public ContentBody getContentBody() {
        return contentBody;
    }

    public void setContentBody(ContentBody contentBody) {
        this.contentBody = contentBody;
    }


    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getHits() {
        return this.hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String body_;

    @Transient
    public String getBody_() {
        return body_;
    }

    public void setBody_(String body) {
        body_ = body;
    }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getBelongCode() {
        return belongCode;
    }

    public void setBelongCode(String belongCode) {
        this.belongCode = belongCode;
    }


}
