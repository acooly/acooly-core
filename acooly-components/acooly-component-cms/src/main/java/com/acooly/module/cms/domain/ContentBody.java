package com.acooly.module.cms.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * 内容主体 Entity
 * <p>
 * Date: 2013-07-12 15:06:46
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "CMS_CONTENT_BODY")
@JsonIgnoreProperties(value = {"content"})
public class ContentBody extends AbstractEntity {
    /**
     * UID
     */
    private static final long serialVersionUID = -1561944102999714790L;
    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "foreignKey", strategy = "foreign", parameters = @Parameter(name = "property", value = "content"))
    @GeneratedValue(generator = "foreignKey")
    private Long id;
    /**
     * 内容主体
     */
    private String body;

    /**
     * 内容主表
     */
    @OneToOne(mappedBy = "contentBody")
    private Content content;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


}
