package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * lottery_count Entity
 *
 * @author Acooly Code Generator Date: 2015-11-28 17:08:35
 */
@Entity
@Table(
        name = "lottery_count",
        indexes = {
                @Index(columnList = "ukey", name = "UK_L_COUNT_UKEY", unique = true),
                @Index(columnList = "lottery_id", name = "IDX_LCOUNT_LID")
        }
)
public class LotteryCount extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

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
     * 关键字
     */
    @Column(
            name = "ukey",
            length = 64,
            nullable = false,
            columnDefinition = "varchar(64)  not null comment '关键字'"
    )
    private String ukey;
    /**
     * 计数值
     */
    @Column(name = "count", nullable = false, columnDefinition = "int not null comment '计数值'")
    private int count;
    /**
     * 备注
     */
    @Column(
            name = "comments",
            length = 128,
            nullable = true,
            columnDefinition = "varchar(128) null comment '备注'"
    )
    private String comments;

    public LotteryCount() {
        super();
    }

    public LotteryCount(Long lotteryId, Long awardId, String ukey) {
        super();
        this.lotteryId = lotteryId;
        this.awardId = awardId;
        this.ukey = ukey;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

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

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }
}
