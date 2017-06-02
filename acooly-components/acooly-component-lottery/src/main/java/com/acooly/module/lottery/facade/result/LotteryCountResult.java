/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-13 13:57 创建
 */
package com.acooly.module.lottery.facade.result;

import com.acooly.core.common.facade.ResultBase;

/** @author acooly */
public class LotteryCountResult extends ResultBase {

  /** 抽奖活动编码 */
  private String lotteryCode;

  /** 抽奖用户 */
  private String username;

  /** 总获得次数 */
  private int totalTimes = 0;

  /** 已抽次数 */
  private int playTimes = 0;

  public String getLotteryCode() {
    return lotteryCode;
  }

  public void setLotteryCode(String lotteryCode) {
    this.lotteryCode = lotteryCode;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getTotalTimes() {
    return totalTimes;
  }

  public void setTotalTimes(int totalTimes) {
    this.totalTimes = totalTimes;
  }

  public int getPlayTimes() {
    return playTimes;
  }

  public void setPlayTimes(int playTimes) {
    this.playTimes = playTimes;
  }
}
