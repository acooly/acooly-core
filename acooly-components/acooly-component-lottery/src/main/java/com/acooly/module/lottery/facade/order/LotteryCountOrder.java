/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-13 13:53 创建
 */
package com.acooly.module.lottery.facade.order;

import com.acooly.core.common.facade.OrderBase;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 可抽奖次数 order
 *
 * <p>当查询时使用，则count可为空，不使用。
 *
 * @author acooly
 */
public class LotteryCountOrder extends OrderBase {

  @NotEmpty private String lotteryCode;

  /** 抽奖用户 */
  @NotEmpty private String username;
  /** 次数 */
  private int count;

  public LotteryCountOrder() {}

  public LotteryCountOrder(String lotteryCode, String username) {
    this.lotteryCode = lotteryCode;
    this.username = username;
  }

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

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
