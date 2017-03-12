/**
 * create by zhangpu
 * date:2015年11月26日
 */
package com.acooly.module.lottery.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author zhangpu
 * @date 2015年11月26日
 */
public class LotteryOrder {
	/** 活动编码 */
	@NotEmpty
	private String code;
	/** 抽奖人 */
	@NotEmpty
	private String user;
	/** 备注 */
	private String comments;

	public LotteryOrder() {
		super();
	}

	public LotteryOrder(String code, String user, String comments) {
		super();
		this.code = code;
		this.user = user;
		this.comments = comments;
	}

	public LotteryOrder(String code, String user) {
		super();
		this.code = code;
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return String.format("LotteryOrder: {code:%s, user:%s, comments:%s}", code, user, comments);
	}

}
