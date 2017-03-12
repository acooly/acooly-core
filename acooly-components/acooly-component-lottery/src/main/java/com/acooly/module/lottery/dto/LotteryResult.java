/**
 * create by zhangpu
 * date:2015年11月26日
 */
package com.acooly.module.lottery.dto;

import com.acooly.module.lottery.domain.LotteryAward;

/**
 * @author zhangpu
 * @date 2015年11月26日
 */
public class LotteryResult {

	private boolean success;

	private String code;

	private String message;

	/** 用户标志 */
	private String user;

	/** 角度位置（0-360度） */
	private int position;

	/** 奖项信息 */
	private LotteryAward award;

	/** 计数奖项标识 */
	private String ukey;

	public LotteryResult() {
		super();
	}

	public LotteryResult(String user, int position, LotteryAward award) {
		super();
		this.user = user;
		this.position = position;
		this.award = award;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public LotteryAward getAward() {
		return award;
	}

	public void setAward(LotteryAward award) {
		this.award = award;
	}

	public String getUkey() {
		return this.ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return String.format("LotteryResult: {user:%s, position:%s, award:%s}", user, position, award);
	}

}
