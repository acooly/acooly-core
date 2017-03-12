package com.acooly.module.lottery.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.acooly.core.utils.enums.SimpleStatus;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acooly.core.common.enums.EntityStatus;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Strings;
import com.acooly.module.lottery.dao.LotteryDao;
import com.acooly.module.lottery.domain.Lottery;
import com.acooly.module.lottery.domain.LotteryAward;
import com.acooly.module.lottery.domain.LotteryCount;
import com.acooly.module.lottery.domain.LotteryUserCount;
import com.acooly.module.lottery.domain.LotteryWhitelist;
import com.acooly.module.lottery.domain.LotteryWinner;
import com.acooly.module.lottery.dto.LotteryOrder;
import com.acooly.module.lottery.dto.LotteryResult;
import com.acooly.module.lottery.enums.LotteryStatus;
import com.acooly.module.lottery.enums.LotteryWhitelistStatus;
import com.acooly.module.lottery.enums.MaxPeriod;
import com.acooly.module.lottery.enums.WinnerStatus;
import com.acooly.module.lottery.exception.NotOpportunityLotteryException;
import com.acooly.module.lottery.exception.VoteLotteryException;
import com.acooly.module.lottery.service.LotteryAwardService;
import com.acooly.module.lottery.service.LotteryCountService;
import com.acooly.module.lottery.service.LotteryService;
import com.acooly.module.lottery.service.LotteryUserCountService;
import com.acooly.module.lottery.service.LotteryWhitelistService;
import com.acooly.module.lottery.service.LotteryWinnerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("lotteryService")
public class LotteryServiceImpl extends EntityServiceImpl<Lottery, LotteryDao> implements LotteryService {

	private static final Logger logger = LoggerFactory.getLogger(LotteryServiceImpl.class);

	@Resource
	private LotteryAwardService lotteryAwardService;
	@Resource
	private LotteryWinnerService lotteryWinnerService;
	@Resource
	private LotteryCountService lotteryCountService;
	@Resource
	private LotteryWhitelistService lotteryWhitelistService;
	@Resource
	private LotteryUserCountService lotteryUserCountService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public LotteryResult lottery(LotteryOrder order) {
		String code = order.getCode();
		String user = order.getUser();
		String comments = order.getComments();
		LotteryResult result = new LotteryResult();
		try {
			Lottery lottery = getEntityDao().findByCode(code);
			checkLottery(lottery, order);

			Long lotteryId = lottery.getId();
			List<LotteryAward> awards = getAwards(lotteryId);
			if (Collections3.isEmpty(awards)) {
				throw new RuntimeException("本周期奖项已完，请等待下个周期再抽奖");
			}
			LotteryAward award = null;
			LotteryCount counter = null;

			// 抽奖
			LotteryWhitelist whitelist = lotteryWhitelistService.getValid(lotteryId, order.getUser());
			if (whitelist != null) {
				award = lotteryAwardService.get(whitelist.getAwardId());
				whitelist.setStatus(LotteryWhitelistStatus.finish);
				lotteryWhitelistService.saveInNewTrans(whitelist);
			} else {
				// 如果抽中的奖项已超过最大中奖数，则重新抽奖，
				// 注意，這裡要求至少一個獎項的最大數量要設置為无限，否則會出現死循環（一般是未中奖这个奖项）。
				award = (awards.get(0));
			}

			// lock
			counter = getAwardCounter(award);
			if ((award.getMaxWiner() > 0 && counter.getCount() >= award.getMaxWiner())) {
				throw new VoteLotteryException();
			}

			// 保存中奖记录
			saveLotteryWinner(lottery, award, user, comments);
			lotteryCountService.appendCount(counter.getUkey());

			// 如果达到活动最大抽奖人数，则停止
			if (lottery.getMaxWinners() > 0) {
				if (lotteryCountService.getCount(lottery.getId()) >= lottery.getMaxWinners()) {
					updateStatus(lotteryId, LotteryStatus.disable);
					logger.info("抽奖活动参与人数上限达到，自动关闭活动:{}", LotteryStatus.disable);
				}
			}
			result.setUser(user);
			result.setAward(award);
			result.setPosition(getPosition(award.getAwardPosition()));
			result.setUkey(counter.getUkey());
			logger.info("抽奖成功:{}", result);
			return result;
		} catch (VoteLotteryException e) {
			throw e;
		} catch (RuntimeException e) {
			logger.error("抽奖失败:{}", e.getMessage());
			throw e;
		} catch (Exception ex) {
			logger.error("系统错误:{}", ex.getMessage());
			throw new RuntimeException("系统错误,请稍后重试!");
		}
	}

	/**
	 * 保持中奖记录
	 * @param lottery
	 * @param award
	 * @param user
	 * @param comments
	 */
	protected void saveLotteryWinner(Lottery lottery, LotteryAward award, String user, String comments) {
		if (award.getRecordWinner() == SimpleStatus.disable) {
			return;
		}
		LotteryWinner lotteryWinner = new LotteryWinner();
		lotteryWinner.setAward(award.getAward());
		lotteryWinner.setAwardId(award.getId());
		lotteryWinner.setLotteryId(lottery.getId());
		lotteryWinner.setLotteryTitle(lottery.getTitle());
		lotteryWinner.setStatus(WinnerStatus.winning);
		lotteryWinner.setWinner(user);
		lotteryWinner.setComments(comments);
		lotteryWinner.setAwardType(award.getAwardType());
		lotteryWinner.setAwardAmount(award.getAwardAmount());
		lotteryWinnerService.save(lotteryWinner);
	}

	/**
	 * 获取award的计数
	 * @param award
	 * @return
	 */
	protected LotteryCount getAwardCounter(LotteryAward award) {
		String ukey = getUkey(award);
		LotteryCount lotteryCount = lotteryCountService.loadAndLock(ukey);
		if (lotteryCount == null) {
			try {
				lotteryCount = new LotteryCount(award.getLotteryId(), award.getId(), ukey);
				lotteryCountService.save(lotteryCount);
				lotteryCount = lotteryCountService.loadAndLock(ukey);
			} catch (Exception e) {
				logger.warn("初存失败,并发（分布式）保存键冲突，忽略错误:{}", e.getMessage());
			}
		}
		return lotteryCount;
	}

	protected int getCount(LotteryAward award) {
		String ukey = getUkey(award);
		LotteryCount lotteryCount = lotteryCountService.load(ukey);
		if (lotteryCount == null) {
			return 0;
		}
		return lotteryCount.getCount();
	}

	protected List<LotteryAward> getAwards(Long lotteryId) {
		List<LotteryAward> lotteryAwards = lotteryAwardService.findBylotteryId(lotteryId);
		List<LotteryAward> awards = Lists.newArrayList();
		for (LotteryAward award : lotteryAwards) {
			// if (award.getMaxWiner() == 0 || getCount(award) <
			// award.getMaxWiner()) {
			awards.add(award);
			// }
		}
		return awards;
	}

	protected String getUkey(LotteryAward award) {
		String ukey = award.getLotteryId() + "" + award.getId();
		String postfix = award.getMaxPeriod().getCode();
		Date date = new Date();
		if (Strings.isNotBlank(award.getMaxPeriod().getPatten())) {
			postfix += Dates.format(date, award.getMaxPeriod().getPatten());
		}
		if (award.getMaxPeriod() == MaxPeriod.quarter) {
			postfix += Dates.getQuarter(date);
		} else if (award.getMaxPeriod() == MaxPeriod.week) {
			postfix += Dates.getWeekOfYear(date);
		}
		return ukey + postfix;
	}

	/**
	 * 抽奖业务check
	 * @param lottery
	 * @param order
	 */
	protected void checkLottery(Lottery lottery, LotteryOrder order) {
		// 状态check
		if (LotteryStatus.enable != lottery.getStatus()) {
			throw new RuntimeException("活动" + lottery.getStatus().getMessage());
		}
		
		// 有效时间check
		Date now = new Date();
		if (now.getTime() < lottery.getStartTime().getTime()) {
			throw new RuntimeException("活动还未开始");
		}
		if (now.getTime() > lottery.getEndTime().getTime()) {
			updateStatus(lottery.getId(), LotteryStatus.disable);
			throw new RuntimeException("活动已过期");
		}
		
		// 用户最大参与次数check
		if (lottery.getMultiPlay() > 0) {
			if (lotteryWinnerService.getWinnerCount(lottery.getId(), order.getUser()) >= lottery.getMultiPlay()) {
				throw new RuntimeException("您的参与次数已满");
			}
		}
		checkUserCount(lottery, order);
		
	}
	
	/**
	 * 检查用户有效参与次数
	 * @param lottery
	 * @param order
	 */
	protected void checkUserCount(Lottery lottery, LotteryOrder order){
		if(lottery.getUserCounter() == null || lottery.getUserCounter() == SimpleStatus.disable){
			return;
		}
		//lock
		LotteryUserCount lotteryUserCount =  lotteryUserCountService.findAndLockUniqueUser(lottery.getId(),order.getUser());
		if(lotteryUserCount == null || lotteryUserCount.getValidTimes() <= 0){
			throw new NotOpportunityLotteryException();
		}else{
			lotteryUserCount.setPlayTimes(lotteryUserCount.getPlayTimes() + 1);
			lotteryUserCountService.save(lotteryUserCount);
		}
	}

	@Deprecated
	@Transactional
	@Override
	public Map<String, Object> lottery(String code, String user) {
		return lottery(code, user, null);
	}

	@Deprecated
	@Transactional
	@Override
	public Map<String, Object> lottery(String code, String user, String comments) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			Lottery lottery = getEntityDao().findByCode(code);
			Long lotteryId = lottery.getId();
			if (LotteryStatus.enable != lottery.getStatus()) {
				throw new RuntimeException("活动" + lottery.getStatus().getMessage());
			}
			Date now = new Date();
			if (now.getTime() < lottery.getStartTime().getTime()) {
				throw new RuntimeException("活动还未开始");
			}

			if (now.getTime() > lottery.getEndTime().getTime()) {
				updateStatus(lotteryId, LotteryStatus.disable);
				logger.info("抽奖活动已过期，自动关闭活动:{}", LotteryStatus.disable);
				throw new RuntimeException("活动已过期");
			}

			if (lotteryWinnerService.getWinnerCount(lotteryId, user) > 0) {
				throw new RuntimeException("您已经抽过奖了");
			}
			List<LotteryAward> awards = lotteryAwardService.findBylotteryId(lotteryId);
			LotteryAward award = null;
			// 如果抽中的奖项已超过最大中奖数，则重新抽奖，注意，這裡要求至少一個獎項的最大數量要設置為無線，否則會出現死循環（一般是未中奖这个奖项）。
			do {
				award = doLottery(awards);
			} while (award.getMaxWiner() > 0
					&& lotteryWinnerService.count(lotteryId, award.getId()) >= award.getMaxWiner());

			LotteryWinner lotteryWinner = new LotteryWinner();
			lotteryWinner.setAward(award.getAward());
			lotteryWinner.setAwardId(award.getId());
			lotteryWinner.setLotteryId(lotteryId);
			lotteryWinner.setLotteryTitle(lottery.getTitle());
			lotteryWinner.setStatus(WinnerStatus.winning);
			lotteryWinner.setWinner(user);
			lotteryWinner.setUpdateTime(new Date());
			lotteryWinner.setComments(comments);
			lotteryWinnerService.save(lotteryWinner);

			// 如果达到活动最大抽奖人数，则停止?
			if (lottery.getMaxWinners() > 0) {
				if (lotteryWinnerService.getCount(lottery.getId()) >= lottery.getMaxWinners()) {
					updateStatus(lotteryId, LotteryStatus.disable);
					logger.info("抽奖活动参与人数上限达到，自动关闭活动:{}", LotteryStatus.disable);
				}
			}

			result.put("winner", user);
			result.put("note", award.getAwardNote());
			result.put("position", getPosition(award.getAwardPosition()));
			return result;
		} catch (Exception e) {
			logger.error("抽奖失败:{}", e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Lottery findByCode(String code) {
		return getEntityDao().findByCode(code);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Lottery updateStatus(Long id, LotteryStatus lotteryStatus) {
		Lottery lottery = get(id);
		lottery.setStatus(lotteryStatus);
		save(lottery);
		return lottery;
	}

	private int getPosition(String position) {
		String[] pos = position.split(",");
		int start = Integer.parseInt(pos[0]);
		int end = Integer.parseInt(pos[1]);
		if (start <= end) {
			return start + RandomUtils.nextInt(end - start);
		} else {
			return RandomUtils.nextBoolean() ? start + RandomUtils.nextInt(360 - start) : RandomUtils.nextInt(end);
		}
	}

	private LotteryAward doLottery(List<LotteryAward> awards) {
		LotteryAward hit = null;
		int weights = 0;
		for (LotteryAward award : awards) {
			weights += award.getWeight();
		}
		int randomInt = RandomUtils.nextInt(weights) + 1;
		int sum = 0;
		for (LotteryAward award : awards) {
			sum += award.getWeight();
			if (randomInt <= sum) {
				hit = award;
				break;
			}
		}
		return hit;
	}
}
