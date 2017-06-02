/** create by zhangpu date:2015年11月30日 */
package com.acooly.module.lottery.exception;

/**
 * 抽奖异常
 *
 * @author zhangpu
 * @date 2016年03月15日
 */
public class LotteryException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = 3350243421569983052L;

  private String code;

  private String message;

  public LotteryException() {
    super();
  }

  /**
   * @param message
   * @param cause
   */
  public LotteryException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.message = message;
  }

  /** @param message */
  public LotteryException(LotteryErrorCode lotteryErrorCode, Throwable cause) {
    this(lotteryErrorCode.getCode(), lotteryErrorCode.getMessage(), cause);
  }

  public LotteryException(LotteryErrorCode lotteryErrorCode) {
    this(lotteryErrorCode, null);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
