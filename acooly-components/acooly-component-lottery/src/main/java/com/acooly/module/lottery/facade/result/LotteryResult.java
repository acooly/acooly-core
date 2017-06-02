/** create by zhangpu date:2015年11月26日 */
package com.acooly.module.lottery.facade.result;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.module.lottery.dto.LotteryAwardInfo;

/**
 * @author zhangpu
 * @date 2015年11月26日
 */
public class LotteryResult extends ResultBase {
  /** 用户标志 */
  private String user;

  /** 角度位置（0-360度） */
  private int position;

  /** 奖项信息 */
  private LotteryAwardInfo award;

  /** 计数奖项标识 */
  private String ukey;

  public LotteryResult() {
    super();
  }

  public LotteryResult(String user, int position, LotteryAwardInfo award) {
    super();
    this.user = user;
    this.position = position;
    this.award = award;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public LotteryAwardInfo getAward() {
    return award;
  }

  public void setAward(LotteryAwardInfo award) {
    this.award = award;
  }

  public String getUkey() {
    return this.ukey;
  }

  public void setUkey(String ukey) {
    this.ukey = ukey;
  }
}
