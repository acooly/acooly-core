/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-12-19
 *
 */
package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * app Entity
 *
 * @author acooly Date: 2016-12-19 21:09:09
 */
@Entity
@Table(name = "app")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class App extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;
  /** id */
  @Id @GeneratedValue private Long id;
  /** display_name */
  private String displayName;
  /** name */
  private String name;
  /** parent_app_id */
  private Long parentAppId;
  /** type */
  private String type;
  /** user_id */
  private Long userId;
  /** 创建时间 */
  private Date rawAddTime = new Date();
  /** 修改时间 */
  private Date rawUpdateTime = new Date();
  /** parent_id */
  private Long parentId;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getParentAppId() {
    return this.parentAppId;
  }

  public void setParentAppId(Long parentAppId) {
    this.parentAppId = parentAppId;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getRawAddTime() {
    return this.rawAddTime;
  }

  public void setRawAddTime(Date rawAddTime) {
    this.rawAddTime = rawAddTime;
  }

  public Date getRawUpdateTime() {
    return this.rawUpdateTime;
  }

  public void setRawUpdateTime(Date rawUpdateTime) {
    this.rawUpdateTime = rawUpdateTime;
  }

  public Long getParentId() {
    return this.parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
