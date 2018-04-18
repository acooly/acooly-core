/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-13 00:21 创建
 */
package com.acooly.core.test.portal;

import com.acooly.module.lottery.portal.AbstractLotteryPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author acooly
 */
@Controller
@RequestMapping("/portal/lottery")
public class LotteryPortalController extends AbstractLotteryPortalController {

    @Override
    protected String getLotteryCode(HttpServletRequest request) {
        // 本抽奖活动的唯一编码。
        return "20150316000000000001";
    }

    @Override
    protected String getLotteryUser(HttpServletRequest request) {
        // 正式环境应从会话中获取当前登录的用户的唯一标志（最好为userName，便于识别）
        return "zhangpu";
    }

    @Override
    protected void doLotteryCheck(HttpServletRequest request) {
        // 做一些前置检查。不包括抽奖次数的控制。如：是否实名，是否有投资，是否额度足够等任何条件，如果检查不通过则直接抛出异常。
    }
}
