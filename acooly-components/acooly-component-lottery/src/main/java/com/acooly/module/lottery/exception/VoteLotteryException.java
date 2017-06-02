/** create by zhangpu date:2015年11月30日 */
package com.acooly.module.lottery.exception;

/**
 * @author zhangpu
 * @date 2015年11月30日
 */
public class VoteLotteryException extends LotteryException {

  /** serialVersionUID */
  private static final long serialVersionUID = 3350243421569983052L;

  public VoteLotteryException() {
    super(LotteryErrorCode.VoteFail);
  }
}
