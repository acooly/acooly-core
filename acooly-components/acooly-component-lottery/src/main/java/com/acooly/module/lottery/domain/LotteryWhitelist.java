package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.lottery.enums.LotteryWhitelistStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * lottery_whitelist Entity
 *
 * @author Acooly Code Generator Date: 2016-01-02 23:20:21
 */
@Entity
@Table(name = "lottery_whitelist")
public class LotteryWhitelist extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 抽奖ID
     */
    private Long lotteryId;
    /**
     * 奖项ID
     */
    private Long awardId;
    /**
     * 抽奖用户
     */
    private String user;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private LotteryWhitelistStatus status = LotteryWhitelistStatus.apply;
    /**
     * 备注
     */
    private String comments;


    public Long getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getAwardId() {
        return this.awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LotteryWhitelistStatus getStatus() {
        return status;
    }

    public void setStatus(LotteryWhitelistStatus status) {
        this.status = status;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
