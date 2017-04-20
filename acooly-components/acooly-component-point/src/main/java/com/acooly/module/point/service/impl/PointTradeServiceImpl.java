/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Ids;
import com.acooly.module.point.dao.PointTradeDao;
import com.acooly.module.point.domain.PointAccount;
import com.acooly.module.point.domain.PointStatistics;
import com.acooly.module.point.domain.PointTrade;
import com.acooly.module.point.enums.PointStaticsStatus;
import com.acooly.module.point.enums.PointTradeType;
import com.acooly.module.point.service.PointAccountService;
import com.acooly.module.point.service.PointStatisticsService;
import com.acooly.module.point.service.PointTradeService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 积分交易信息 Service实现
 *
 * Date: 2017-02-03 22:50:14
 *
 * @author cuifuqiang
 *
 */
@Service("pointTradeService")
public class PointTradeServiceImpl extends EntityServiceImpl<PointTrade, PointTradeDao> implements PointTradeService {

	private static final Logger logger = LoggerFactory.getLogger(PointTradeServiceImpl.class);

	@Autowired
	private PointAccountService pointAccountService;
	@Autowired
	private PointStatisticsService pointStatisticsService;

	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public PointTrade pointProduce(String userName, long point, String businessData) {
		PointAccount pointAccount = pointAccountService.pointProduce(userName, point);
		PointTrade pointTrade = saveTrade(pointAccount, point, PointTradeType.produce, businessData);
		return pointTrade;
	}

	@Override
	public PointTrade pointExpense(String userName, long point, boolean isFreeze, String businessData) {
		PointAccount pointAccount = pointAccountService.pointExpense(userName, point, isFreeze);
		if (isFreeze) {
			saveTrade(pointAccount, point, PointTradeType.unfreeze, businessData);
		}
		PointTrade pointTrade = saveTrade(pointAccount, point, PointTradeType.expense, businessData);
		return pointTrade;
	}

	@Override
	public PointTrade pointFreeze(String userName, long point, String businessData) {
		PointAccount pointAccount = pointAccountService.pointFreeze(userName, point);
		PointTrade pointTrade = saveTrade(pointAccount, point, PointTradeType.freeze, businessData);
		return pointTrade;

	}

	@Override
	public PointTrade pointUnfreeze(String userName, long point, String businessData) {
		PointAccount pointAccount = pointAccountService.pointUnfreeze(userName, point);
		PointTrade pointTrade = saveTrade(pointAccount, point, PointTradeType.unfreeze, businessData);
		return pointTrade;
	}

	public long getClearPoint(String userName, String startTime, String endTime) {
		return getEntityDao().getClearPoint(userName, startTime, endTime);
	}

	public void pointClearThread(String startTime, String endTime, String businessData) {
		logger.info("启动新建线程,处理积分清零");
		taskExecutor.execute(() -> {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				logger.warn("启动新建线程,处理积分清零失败");
			}
			pointClear(startTime, endTime, businessData);
		});
	}

	public void pointClear(String startTime, String endTime, String businessData) {
		pointStatisticsService.pointStatistics(startTime, endTime);
		PageInfo<PointStatistics> pageInfo = new PageInfo<PointStatistics>();
		Map<String, Object> maps = Maps.newHashMap();
		maps.put("GTE_startTime", startTime);
		maps.put("LTE_endTime", endTime);
		PageInfo<PointStatistics> pageInfos = pointStatisticsService.query(pageInfo, maps);
		long totalPage = pageInfos.getTotalPage();
		for (int i = 0; i < totalPage + 1; i++) {
			pageInfo.setCurrentPage(1);
			PageInfo<PointStatistics> pages = pointStatisticsService.query(pageInfo, maps);
			for (PointStatistics pointStatistics : pages.getPageResults()) {
				if (pointStatistics.getStatus() == PointStaticsStatus.init) {
					Long point = pointStatistics.getPoint();
					pointStatistics.setStatus(PointStaticsStatus.finish);
					PointAccount pointAccount = pointAccountService.findByUserName(pointStatistics.getUserName());
					Long availablePoint = pointAccount.getAvailable();
					if (availablePoint <= point) {
						pointAccount.setBalance(pointAccount.getBalance() - availablePoint);
						pointStatistics.setActualPoint(availablePoint);
					} else {
						pointAccount.setBalance(pointAccount.getBalance() - point);
						pointStatistics.setActualPoint(point);
					}
					pointAccountService.update(pointAccount);
					pointStatisticsService.update(pointStatistics);
					saveTrade(pointAccount, pointStatistics.getActualPoint(), PointTradeType.clear, businessData);
				}
			}
		}
	}

	private PointTrade saveTrade(PointAccount pointAccount, long point, PointTradeType tradeType, String businessData) {
		PointTrade pointTrade = new PointTrade();
		pointTrade.setTradeNo(Ids.oid());
		pointTrade.setAccountId(pointAccount.getId());
		pointTrade.setUserName(pointAccount.getUserName());
		pointTrade.setTradeType(tradeType);
		pointTrade.setAmount(point);
		pointTrade.setEndFreeze(pointAccount.getFreeze());
		pointTrade.setEndBalance(pointAccount.getBalance());
		pointTrade.setEndAvailable(pointAccount.getAvailable());
		pointTrade.setBusinessData(businessData);
		getEntityDao().create(pointTrade);
		return pointTrade;
	}
}
