/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 *
 */
package com.acooly.module.point.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointTrade;

/**
 * 积分交易信息 Service接口
 *
 * Date: 2017-02-03 22:50:14
 * 
 * @author cuifuqiang
 *
 */
public interface PointTradeService extends EntityService<PointTrade> {

	/**
	 * 积分产生
	 * 
	 * @param userName
	 *            用户名
	 * @param point
	 *            交易积分(产生)
	 * @param businessData
	 *            业务数据
	 */
	public PointTrade pointProduce(String userName, long point, String businessData);

	/**
	 * 积分消费
	 * 
	 * @param userName
	 *            用户名
	 * @param point
	 *            交易积分(消费)
	 * @param isFreeze
	 *            是否存在冻结（true:存在冻结积分,false:不存在冻结积分）
	 * @param businessData
	 *            业务数据
	 * @return
	 */
	public PointTrade pointExpense(String userName, long point, boolean isFreeze, String businessData);

	/**
	 * 积分冻结
	 * 
	 * @param userName
	 *            用户名
	 * @param point
	 *            交易积分(冻结积分)
	 * @param businessData
	 *            业务数据
	 * @return
	 */
	public PointTrade pointFreeze(String userName, long point, String businessData);

	/**
	 * 积分解冻
	 * 
	 * @param userName
	 *            用户名
	 * @param point
	 *            交易积分(解冻积分)
	 * @param businessData
	 *            业务数据
	 * @return
	 */
	public PointTrade pointUnfreeze(String userName, long point, String businessData);

	/**
	 * 积分清零
	 * 
	 * @param startTime
	 * @param endTime
	 * @param memo
	 */
	public void pointClear(String startTime, String endTime, String businessData);

}
