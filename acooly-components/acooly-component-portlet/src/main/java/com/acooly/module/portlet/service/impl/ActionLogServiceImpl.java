/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-03-20
 */
package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.module.portlet.dao.ActionLogDao;
import com.acooly.module.portlet.entity.ActionLog;
import com.acooly.module.portlet.entity.ActionMapping;
import com.acooly.module.portlet.enums.ActionChannelEnum;
import com.acooly.module.portlet.service.ActionLogService;
import com.acooly.module.portlet.service.ActionMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * portlet_action_log Service实现
 * <p>
 * Date: 2017-03-20 23:36:29
 *
 * @author acooly
 */
@Service("actionLogService")
public class ActionLogServiceImpl extends EntityServiceImpl<ActionLog, ActionLogDao> implements ActionLogService {

    private static final Logger logger = LoggerFactory.getLogger(ActionLogServiceImpl.class);

    @Resource
    private ActionMappingService actionMappingService;

    @Override
    public ActionLog logger(String action, String actionName, String userName, ActionChannelEnum actionChannel, String version, String comments, HttpServletRequest request) {
        try {
            ActionLog actionLog = new ActionLog();
            actionLog.setActionKey(action);
            actionLog.setActionName(actionName);
            if (actionChannel == null && request == null) {
                throw new IllegalArgumentException("actionChannel和request不能同时为空");
            }
            if (actionChannel == null && request != null) {
                actionLog.setChannel(parseChannelForReqeust(request));
            } else {
                actionLog.setChannel(actionChannel);
            }
            actionLog.setComments(comments);
            actionLog.setUserName(userName);
            actionLog.setChannelVersion(version);
            actionLog.setUserIp(IPUtil.getIpAddr(request));
            if (request != null) {
                actionLog.setChannelInfo(Strings.substring(Servlets.getHeaderValue(request, "User-Agent"), 0, 255));
            }
            save(actionLog);
            return actionLog;
        } catch (Exception e) {
            logger.warn("保持action日志失败:{}", e.getMessage());
        }
        return null;
    }


    @Override
    public ActionLog logger(HttpServletRequest request, String userName) {
        String actionKey = Servlets.getRequestPath(request);
        String actionName = null;
        ActionMapping actionMapping = actionMappingService.getActionMapping(actionKey);
        if (actionMapping != null) {
            actionName = actionMapping.getTitle();
        }
        return logger(actionKey, actionName, userName, parseChannelForReqeust(request), null, null, request);
    }

    protected ActionChannelEnum parseChannelForReqeust(HttpServletRequest request) {
        String userAgent = Servlets.getHeaderValue(request, "User-Agent");
        if (Strings.contains(userAgent, "MicroMessenger")) {
            return ActionChannelEnum.wechat;
        }
        String[] iosMobiles = {"iPod", "iPad", "iPhone", "Android", "SymbianOS", "Windows Phone"};
        for (String s : iosMobiles) {
            if (Strings.containsIgnoreCase(userAgent, s)) {
                return ActionChannelEnum.wap;
            }
        }
        return ActionChannelEnum.web;
    }
}
