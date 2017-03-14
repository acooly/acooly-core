/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-15 00:22 创建
 */
package com.acooly.module.lottery.event;

import com.acooly.core.utils.ToString;
import com.acooly.module.lottery.domain.LotteryWinner;
import com.acooly.module.lottery.dto.LotteryAwardInfo;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 抽奖事件
 *
 * @author acooly
 */
@Data
public class LotteryEvent {

    @NotNull
    private Long lotteryId;
    @NotEmpty
    private String lotteryCode;
    @NotNull
    private LotteryAwardInfo lotteryAwardInfo;
    /**
     * 但抽奖活动的中奖奖项配置为不记录中奖时，该对象为空
     */
    private LotteryWinner lotteryWinner;

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public LotteryAwardInfo getLotteryAwardInfo() {
        return lotteryAwardInfo;
    }

    public void setLotteryAwardInfo(LotteryAwardInfo lotteryAwardInfo) {
        this.lotteryAwardInfo = lotteryAwardInfo;
    }

    public LotteryWinner getLotteryWinner() {
        return lotteryWinner;
    }

    public void setLotteryWinner(LotteryWinner lotteryWinner) {
        this.lotteryWinner = lotteryWinner;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
