package com.acooly.module.lottery.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.module.lottery.enums.LotteryStatus;
import com.acooly.module.lottery.enums.LotteryType;

import javax.persistence.*;
import java.util.Date;

/**
 * 抽奖 Entity
 *
 * @author zhangpu
 */
@Entity
@Table(name = "lottery", schema = "", indexes = {@Index(name = "uk_lottery_code", unique = true, columnList = "code")})
public class Lottery extends AbstractEntity {
    /**
     * UID
     */
    private static final long serialVersionUID = -6871704191327989331L;

    /**
     * 编码
     */
    @Column(name = "code", length = 32, nullable = false, columnDefinition = "varchar(32)  not null comment '编码'")
    private String code;

    /**
     * 标题
     */
    @Column(name = "title", length = 64, nullable = false, columnDefinition = "varchar(64)  not null comment '标题'")
    private String title;

    /**
     * 标题
     */
    @Column(name = "note", length = 512, nullable = true, columnDefinition = "varchar(512) comment '说明'")
    private String note;

    /**
     * 类型
     */
    @Column(name = "type", length = 16, nullable = false, columnDefinition = "varchar(16) not null comment'类型'")
    @Enumerated(EnumType.STRING)
    private LotteryType type = LotteryType.roulette;

    /**
     * 最大中奖人数,0为无限制
     */
    @Column(name = "max_winners", nullable = true, columnDefinition = "int  not null comment '最大中奖数'")
    private int maxWinners = 0;

    /**
     * 可参与次数,0:无限制
     */
    @Column(name = "multi_play", nullable = true, columnDefinition = "int  not null comment '可参与次数'")
    private int multiPlay = 0;

    /**
     * 开始时间
     */
    @Column(name = "start_time", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '开始时间'")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '结束时间'")
    private Date endTime;

    /**
     * 是否开启用户参与计数检查
     */
    @Column(name = "user_counter", length = 16, nullable = false, columnDefinition = "varchar(16) not null comment'是否参与计数'")
    @Enumerated(EnumType.STRING)
    private SimpleStatus userCounter = SimpleStatus.disable;

    /**
     * 状态
     */
    @Column(name = "status", length = 16, nullable = false, columnDefinition = "varchar(16) not null comment'状态'")
    @Enumerated(EnumType.STRING)
    private LotteryStatus status = LotteryStatus.enable;

    /**
     * 发布事件标志
     *
     * 开启后，抽奖成功会发布事件，便于后续业务处理扩展。
     */
    private SimpleStatus publishEvent = SimpleStatus.disable;

    /**
     * 备注
     */
    @Column(name = "comments", length = 128, nullable = true, columnDefinition = "varchar(128) comment '备注'")
    private String comments;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Enumerated(EnumType.STRING)
    public LotteryType getType() {
        return type;
    }

    public void setType(LotteryType type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public LotteryStatus getStatus() {
        return status;
    }

    public void setStatus(LotteryStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMaxWinners() {
        return maxWinners;
    }

    public void setMaxWinners(int maxWinners) {
        this.maxWinners = maxWinners;
    }

    public int getMultiPlay() {
        return multiPlay;
    }

    public void setMultiPlay(int multiPlay) {
        this.multiPlay = multiPlay;
    }

    public SimpleStatus getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(SimpleStatus userCounter) {
        this.userCounter = userCounter;
    }

    public SimpleStatus getPublishEvent() {
        return publishEvent;
    }

    public void setPublishEvent(SimpleStatus publishEvent) {
        this.publishEvent = publishEvent;
    }
}
