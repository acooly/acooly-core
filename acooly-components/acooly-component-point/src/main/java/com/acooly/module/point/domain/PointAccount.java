/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.point.enums.PointAccountStatus;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

/**
 * 积分账户 Entity
 *
 * @author cuifuqiang Date: 2017-02-03 22:45:13
 */
@Entity
@Table(name = "point_account")
public class PointAccount extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;
  /** ID */
  /** 用户名 */
  private String userName;
  /** 积分余额 */
  private Long balance = 0l;
  /** 冻结 */
  private Long freeze = 0l;
  /** 可用余额 */
  @Transient private Long available = 0l;
  /** 总消费积分 * */
  private Long totalExpensePoint = 0l;
  /** 总产生积分 * */
  private Long totalProducePoint = 0l;
  /** 状态 */
  @Enumerated(EnumType.STRING)
  private PointAccountStatus status = PointAccountStatus.valid;
  /** 积分等级 * */
  private Long gradeId;
  /** 备注 */
  private String memo;

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Long getBalance() {
    return this.balance;
  }

  public void setBalance(Long balance) {
    this.balance = balance;
  }

  public Long getFreeze() {
    return this.freeze;
  }

  public void setFreeze(Long freeze) {
    this.freeze = freeze;
  }

  public Long getAvailable() {
    return this.balance - this.freeze;
  }

  public void setAvailable(Long available) {
    this.available = available;
  }

  public Long getTotalExpensePoint() {
    return totalExpensePoint;
  }

  public void setTotalExpensePoint(Long totalExpensePoint) {
    this.totalExpensePoint = totalExpensePoint;
  }

  public Long getTotalProducePoint() {
    return totalProducePoint;
  }

  public void setTotalProducePoint(Long totalProducePoint) {
    this.totalProducePoint = totalProducePoint;
  }

  public PointAccountStatus getStatus() {
    return status;
  }

  public void setStatus(PointAccountStatus status) {
    this.status = status;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getMemo() {
    return this.memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
