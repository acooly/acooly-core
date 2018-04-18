package com.acooly.module.lottery.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.lottery.domain.LotteryCount;
import com.acooly.module.lottery.service.LotteryCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage/module/lottery/lotteryCount")
public class LotteryCountManagerController
        extends AbstractJQueryEntityController<LotteryCount, LotteryCountService> {

    @Autowired
    private LotteryCountService lotteryCountService;
}
