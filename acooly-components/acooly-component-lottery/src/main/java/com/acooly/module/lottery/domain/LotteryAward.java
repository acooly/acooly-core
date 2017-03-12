package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.module.lottery.enums.LotteryAwardType;
import com.acooly.module.lottery.enums.MaxPeriod;

import javax.persistence.*;

/**
 * lottery_award Entity
 * <p>
 * Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 */
@Entity
@Table(name = "lottery_award", indexes = {@Index(columnList = "code", name = "UK_AWARD_CODE", unique = true)})
public class LotteryAward extends AbstractEntity {
    /**
     * UID
     */
    private static final long serialVersionUID = 603863256203920037L;


    /**
     * 抽奖ID
     */
    @Column(name = "lottery_id", nullable = false, columnDefinition = "bigint  not null comment '抽奖ID'")
    private Long lotteryId;

    /**
     * 编码
     */
    @Column(name = "code", length = 32, nullable = false, columnDefinition = "varchar(32)  not null comment '编码'")
    private String code;

    /**
     * 奖励类型
     */
    @Column(name = "award_type", nullable = false, columnDefinition = "varchar(32) not null comment '奖励类型'")
    @Enumerated(EnumType.STRING)
    private LotteryAwardType awardType;

    /**
     * 奖项
     */
    @Column(name = "award", length = 32, nullable = false, columnDefinition = "varchar(32)  not null comment '奖项'")
    private String award;

    /**
     * 奖项金额(分)
     */
    @Column(name = "award_amount", nullable = false, columnDefinition = "bigint  null comment '奖项金额(分)'")
    private Long awardAmount = 0l;


    /**
     * 奖项说明
     */
    @Column(name = "award_note", length = 256, nullable = true, columnDefinition = "varchar(256)  null comment '奖项说明'")
    private String awardNote;

    /**
     * 奖品图片
     */
    @Column(name = "award_photo", length = 128, nullable = true, columnDefinition = "varchar(128)  null comment '奖项图片'")
    private String awardPhoto;

    /**
     * 奖项位置
     */
    @Column(name = "award_position", length = 32, nullable = true, columnDefinition = "varchar(32)  null comment '奖项位置'")
    private String awardPosition;

    /**
     * 权重
     */
    @Column(name = "weight", nullable = false, columnDefinition = "int  not null comment '权重'")
    private int weight = 0;

    /**
     * 最大中奖数
     */
    @Column(name = "max_winer", nullable = false, columnDefinition = "int not null comment '最大中奖数'")
    private int maxWiner = 0;

    /**
     * 最大中奖数周期
     */
    @Column(name = "max_period", nullable = false, columnDefinition = "varchar(32) not null comment '最大中奖数周期'")
    @Enumerated(EnumType.STRING)
    private MaxPeriod maxPeriod = MaxPeriod.ulimit;

    /**
     * 是否记录中奖记录
     */
    @Column(name = "record_winner", nullable = false, columnDefinition = "varchar(32) not null comment '是否记录中奖记录'")
    @Enumerated(EnumType.STRING)
    private SimpleStatus recordWinner = SimpleStatus.enable;

    /**
     * 备注
     */
    @Column(name = "comments", length = 128, nullable = false, columnDefinition = "varchar(128)  null comment '备注'")
    private String comments;


    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getAwardNote() {
        return awardNote;
    }

    public void setAwardNote(String awardNote) {
        this.awardNote = awardNote;
    }

    public String getAwardPhoto() {
        return awardPhoto;
    }

    public void setAwardPhoto(String awardPhoto) {
        this.awardPhoto = awardPhoto;
    }

    public String getAwardPosition() {
        return awardPosition;
    }

    public void setAwardPosition(String awardPosition) {
        this.awardPosition = awardPosition;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxWiner() {
        return maxWiner;
    }

    public void setMaxWiner(int maxWiner) {
        this.maxWiner = maxWiner;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public MaxPeriod getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(MaxPeriod maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public SimpleStatus getRecordWinner() {
        return recordWinner;
    }

    public void setRecordWinner(SimpleStatus recordWinner) {
        this.recordWinner = recordWinner;
    }

    public String getCode() {
        return this.code;
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

    public Long getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(Long awardAmount) {
        this.awardAmount = awardAmount;
    }

}
