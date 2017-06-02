/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 *
 */
package com.acooly.module.point.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.point.domain.PointAccount;

/**
 * 积分账户 Service接口
 *
 * <p>Date: 2017-02-03 22:45:12
 *
 * @author cuifuqiang
 */
public interface PointAccountService extends EntityService<PointAccount> {

  PointAccount findByUserName(String userName);

  PointAccount pointProduce(String userName, long point);

  PointAccount pointExpense(String userName, long point, boolean isFreeze);

  PointAccount pointFreeze(String userName, long point);

  PointAccount pointUnfreeze(String userName, long point);

  /**
   * 同一个等级的排名
   *
   * @param userName
   * @param gradeId
   * @return
   */
  public int pointRank(String userName, Long gradeId);
}
