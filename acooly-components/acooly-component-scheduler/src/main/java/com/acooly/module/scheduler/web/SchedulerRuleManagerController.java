package com.acooly.module.scheduler.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.scheduler.domain.SchedulerRule;
import com.acooly.module.scheduler.engine.ScheduleEngineImpl;
import com.acooly.module.scheduler.exceptions.SchedulerExecuteException;
import com.acooly.module.scheduler.executor.TaskStatusEnum;
import com.acooly.module.scheduler.executor.TaskTypeEnum;
import com.acooly.module.scheduler.service.SchedulerRuleService;
import com.acooly.module.security.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *  定时任务
 *
 * @author shuijing
 *         Date: 2017-04-06 17:50:51
 */
@Controller
@RequestMapping(value = "/manage/schedulerRule")
public class SchedulerRuleManagerController extends AbstractJQueryEntityController<SchedulerRule, SchedulerRuleService> {

    private static Logger logger = LoggerFactory.getLogger(SchedulerRuleManagerController.class);

    @Resource
    private ScheduleEngineImpl scheduleEngine;

    {
        allowMapping = "*";
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allTaskTypes", TaskTypeEnum.mapping());
        model.put("allStatuss", TaskStatusEnum.mapping());
    }

    @Override
    protected SchedulerRule onSave(HttpServletRequest request, HttpServletResponse response, Model model,
                                   SchedulerRule entity, boolean isCreate) throws Exception {
        if(isCreate){
            entity.setCreater(ShiroUtils.getCurrentUser().getUsername());
            entity.setExecuteNum(0);
        }else {
            entity.setModifyer(ShiroUtils.getCurrentUser().getUsername());
        }
        //执行定时任务规则检查
        try {
            scheduleEngine.validateRule(entity);
        } catch (Exception e) {
            logger.error("更新定时任务规则检验失败：" + e);
            throw new SchedulerExecuteException(e);
        }
        return super.onSave(request, response, model, entity, isCreate);
    }

    @Override
    protected SchedulerRule doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate)
        throws Exception {
        SchedulerRule schedulerRule = super.doSave(request, response, model, isCreate);
        //添加定时任务到quartz引擎
        scheduleEngine.addJobToEngine(schedulerRule);
        return schedulerRule;
    }

    @Override
    public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
        SchedulerRule schedulerRule = getSchedulerRule(request);
        if (schedulerRule == null) {
            JsonResult result = new JsonResult();
            result.setSuccess(false);
            result.setMessage("没有这个定时任务");
            return result;
        }
        scheduleEngine.deleteJob(schedulerRule);
        return super.deleteJson(request, response);
    }

    private SchedulerRule getSchedulerRule(HttpServletRequest request) {
        return getEntityService().get(Long.valueOf(request.getParameter("id")));
    }

    @Override
    public JsonEntityResult<SchedulerRule> updateJson(HttpServletRequest request, HttpServletResponse response) {
        SchedulerRule schedulerRule = getSchedulerRule(request);
        if (schedulerRule == null) {
            logger.error("任务不存在，任务id：[task{}]", schedulerRule.getId());
            JsonEntityResult<SchedulerRule> result = new JsonEntityResult<>();
            result.setSuccess(false);
            result.setMessage("任务不存在");
            return result;
        }
        try {
            scheduleEngine.validateRule(schedulerRule);
        } catch (Exception e) {
            logger.error("更新定时任务规则检验失败：" + e.getMessage());
            JsonEntityResult<SchedulerRule> result = new JsonEntityResult<>();
            result.setSuccess(false);
            result.setMessage("更新定时任务规则检验失败");
            return result;
        }
//        不是自己修改
//        if (!user.getName().equals(rule.getCreater())) {
//            logger.error("越权修改定时任务,任务id：[task{}]", rule.getId());
//            return fail("不是本人，不能修改此定时任务");
//        }

        JsonEntityResult<SchedulerRule> result = super.updateJson(request, response);
        SchedulerRule resultEntity = result.getEntity();

        //开启 就更新quartz
        if (resultEntity.getStatus().equals(TaskStatusEnum.NORMAL.getCode())) {
            //更新调度
            scheduleEngine.update(resultEntity);
        }
        //关闭 就从quartz中删除
        if (resultEntity.getStatus().equals(TaskStatusEnum.CANCELED.getCode())) {
            scheduleEngine.deleteJob(resultEntity);
        }
        return result;
    }
}
