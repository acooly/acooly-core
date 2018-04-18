package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.lottery.enums.LotteryAwardType;
import com.acooly.module.lottery.enums.WinnerStatus;

import javax.persistence.*;

/**
 * lottery_winner Entity
 *
 * <p>Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "lottery_winner")
public class LotteryWinner extends AbstractEntity {
    /**
     * UID
     */
    private static final long serialVersionUID = -7814394877386998862L;

    /**
     * 抽奖ID
     */
    @Column(
            name = "lottery_id",
            nullable = false,
            columnDefinition = "bigint  not null comment '抽奖ID'"
    )
    private Long lotteryId;

    /**
     * 奖项ID
     */
    @Column(name = "award_id", nullable = false, columnDefinition = "bigint  not null comment '奖项ID'")
    private Long awardId;

    /**
     * 抽奖标题
     */
    @Column(
            name = "lottery_title",
            length = 64,
            nullable = false,
            columnDefinition = "varchar(64)  not null comment '抽奖标题'"
    )
    private String lotteryTitle;

    /**
     * 奖励类型
     */
    @Column(
            name = "award_type",
            nullable = false,
            columnDefinition = "varchar(32) not null comment '奖励类型'"
    )
    @Enumerated(EnumType.STRING)
    private LotteryAwardType awardType;

    /**
     * 奖项
     */
    @Column(
            name = "award",
            length = 32,
            nullable = false,
            columnDefinition = "varchar(32)  not null comment '奖项'"
    )
    private String award;

    /**
     * 奖项金额(分)
     */
    @Column(
            name = "award_amount",
            nullable = false,
            columnDefinition = "bigint  null comment '奖项金额(分)'"
    )
    private Long awardAmount = 0l;

    /**
     * 中奖人
     */
    @Column(
            name = "winner",
            length = 32,
            nullable = false,
            columnDefinition = "varchar(32)  not null comment '中奖人'"
    )
    private String winner;

    /**
     * 状态
     */
    @Column(
            name = "status",
            length = 16,
            nullable = false,
            columnDefinition = "varchar(16) not null comment'状态'"
    )
    @Enumerated(EnumType.STRING)
    private WinnerStatus status = WinnerStatus.winning;

    /**
     * 备注
     */
    @Column(
            name = "comments",
            length = 128,
            nullable = false,
            columnDefinition = "varchar(128)  null comment '备注'"
    )
    private String comments;

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public String getLotteryTitle() {
        return lotteryTitle;
    }

    public void setLotteryTitle(String lotteryTitle) {
        this.lotteryTitle = lotteryTitle;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Enumerated(EnumType.STRING)
    public WinnerStatus getStatus() {
        return status;
    }

    public void setStatus(WinnerStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LotteryAwardType getAwardType() {
        return awardType;
    }

    public void setAwardType(LotteryAwardType awardType) {
        this.awardType = awardType;
    }

    public Long getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(Long awardAmount) {
        this.awardAmount = awardAmount;
    }
}
