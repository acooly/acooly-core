package com.acooly.module.scheduler.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.scheduler.domain.SchedulerRule;
import com.acooly.module.scheduler.service.SchedulerRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * scheduler_rule 管理控制器
 *
 * @author shuijing
 *         Date: 2017-04-06 17:50:51
 */
@Controller
@RequestMapping(value = "/manage/schedulerRule")
public class SchedulerRuleManagerController extends AbstractJQueryEntityController<SchedulerRule, SchedulerRuleService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private SchedulerRuleService schedulerRuleService;


}
