/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.point.enums.PointTradeType;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 积分交易信息 Entity
 *
 * @author cuifuqiang Date: 2017-02-03 22:50:14
 */
@Entity
@Table(name = "point_trade")
public class PointTrade extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /** id */
    /**
     * 交易订单号
     */
    private String tradeNo;
    /**
     * 交易类型
     */
    @Enumerated(EnumType.STRING)
    private PointTradeType tradeType;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 积分账户ID
     */
    private Long accountId;
    /**
     * 交易积分
     */
    private Long amount = 0L;
    /**
     * 交易后冻结积分
     */
    private Long endFreeze = 0L;
    /**
     * 交易后积分
     */
    private Long endBalance = 0L;
    /**
     * 交易后有效积分
     */
    private Long endAvailable = 0L;

    /**
     * 相关业务Id *
     */
    private String busiId;
    /**
     * 相关业务类型 *
     */
    private String busiType;
    /**
     * 相关业务类型描述 *
     */
    private String busiTypeText;
    /**
     * 相关业务数据 *
     */
    private String busiData;

    /**
     * 备注
     */
    private String memo;

    public String getTradeNo() {
        return this.tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public PointTradeType getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(PointTradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getEndFreeze() {
        return endFreeze;
    }

    public void setEndFreeze(Long endFreeze) {
        this.endFreeze = endFreeze;
    }

    public Long getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Long endBalance) {
        this.endBalance = endBalance;
    }

    public Long getEndAvailable() {
        return endAvailable;
    }

    public void setEndAvailable(Long endAvailable) {
        this.endAvailable = endAvailable;
    }

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBusiTypeText() {
        return busiTypeText;
    }

    public void setBusiTypeText(String busiTypeText) {
        this.busiTypeText = busiTypeText;
    }

    public String getBusiData() {
        return busiData;
    }

    public void setBusiData(String busiData) {
        this.busiData = busiData;
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
