/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-12 23:55 创建
 */
package com.acooly.module.lottery.dto;

import com.acooly.core.utils.ToString;
import com.acooly.module.lottery.enums.LotteryAwardType;

import java.io.Serializable;

/**
 * @author acooly
 */
public class LotteryAwardInfo implements Serializable {

    private Long id;

    /**
     * 抽奖ID
     */
    private Long lotteryId;

    /**
     * 编码
     */
    private String code;

    /**
     * 奖励类型
     */
    private LotteryAwardType awardType;

    /**
     * 奖项
     */
    private String award;

    /**
     * 奖项金额(分)
     */
    private Long awardValue;

    /**
     * 奖项说明
     */
    private String awardNote;

    /**
     * 奖项位置
     */
    private String awardPosition;

    /**
     * 备注
     */
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LotteryAwardType getAwardType() {
        return awardType;
    }

    public void setAwardType(LotteryAwardType awardType) {
        this.awardType = awardType;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public Long getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(Long awardValue) {
        this.awardValue = awardValue;
    }

    public String getAwardNote() {
        return awardNote;
    }

    public void setAwardNote(String awardNote) {
        this.awardNote = awardNote;
    }

    public String getAwardPosition() {
        return awardPosition;
    }

    public void setAwardPosition(String awardPosition) {
        this.awardPosition = awardPosition;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
