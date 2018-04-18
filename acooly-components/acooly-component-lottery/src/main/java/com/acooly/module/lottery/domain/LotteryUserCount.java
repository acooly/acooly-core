package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * lottery_user_count Entity
 *
 * @author Acooly Code Generator Date: 2016-03-11 02:38:56
 */
@Entity
@Table(
        name = "lottery_user_count",
        indexes = {@Index(columnList = "lottery_id,user", name = "UK_LOTTERY_USER_COUNT", unique = true)}
)
public class LotteryUserCount extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @Column(
            name = "lottery_id",
            nullable = false,
            columnDefinition = "bigint  not null comment '抽奖ID'"
    )
    private Long lotteryId;

    /**
     * 活动唯一编码
     */
    private String lotteryCode;
    /**
     * 活动标题
     */
    @Column(
            name = "lottery_title",
            length = 64,
            nullable = true,
            columnDefinition = "varchar(64) comment '标题'"
    )
    private String lotteryTitle;
    /**
     * 参与人
     */
    @Column(
            name = "user",
            length = 64,
            nullable = false,
            columnDefinition = "varchar(32)  not null comment '参与人'"
    )
    private String user;
    /**
     * 获参次数
     */
    @Column(name = "total_times", nullable = true, columnDefinition = "int  not null comment '获参次数'")
    private int totalTimes = 0;
    /**
     * 参与次数
     */
    @Column(name = "play_times", nullable = true, columnDefinition = "int  not null comment '参与次数'")
    private int playTimes = 0;

    /**
     * 备注
     */
    @Column(
            name = "comments",
            length = 128,
            nullable = true,
            columnDefinition = "varchar(128) comment '备注'"
    )
    private String comments;

    @Transient
    public int getValidTimes() {
        return this.totalTimes - this.playTimes;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryTitle() {
        return this.lotteryTitle;
    }

    public void setLotteryTitle(String lotteryTitle) {
        this.lotteryTitle = lotteryTitle;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTotalTimes() {
        return this.totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public int getPlayTimes() {
        return this.playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }
}
