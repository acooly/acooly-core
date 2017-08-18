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
import com.acooly.module.portlet.PortletProperties;
import com.acooly.module.portlet.dao.ActionLogDao;
import com.acooly.module.portlet.entity.ActionLog;
import com.acooly.module.portlet.entity.ActionMapping;
import com.acooly.module.portlet.enums.ActionChannelEnum;
import com.acooly.module.portlet.service.ActionLogService;
import com.acooly.module.portlet.service.ActionMappingService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * portlet_action_log Service实现
 * <p>
 * <p>Date: 2017-03-20 23:36:29
 *
 * @author acooly
 */
@Service("actionLogService")
@Slf4j
public class ActionLogServiceImpl extends EntityServiceImpl<ActionLog, ActionLogDao>
        implements ActionLogService {

    private static final Logger logger = LoggerFactory.getLogger(ActionLogServiceImpl.class);

    @Resource
    private ActionMappingService actionMappingService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PortletProperties portletProperties;

    private static final String ACTION_LOG_CACHE_NAME = "portlet.action.actionlogs";
    private static final int ACTION_LOG_CACHE_SIZE_DEFAULT = 100;


    @Override
    public ActionLog logger(HttpServletRequest request, String userName) {
        String actionKey = Servlets.getRequestPath(request);
        String actionName = null;
        ActionMapping actionMapping = actionMappingService.getActionMapping(actionKey);
        if (actionMapping != null) {
            actionName = actionMapping.getTitle();
        }
        return logger(
                actionKey, actionName, userName, parseChannelForReqeust(request), null, null, request);
    }

    @Override
    public ActionLog logger(
            String action,
            String actionName,
            String userName,
            ActionChannelEnum actionChannel,
            String version,
            String comments,
            HttpServletRequest request) {

        try {
            ActionLog actionLog = doMarshallActionLog(action, actionName, userName, actionChannel, version, comments, request);
            if (portletProperties.getActionlog().isCacheable()) {
                int cacheSize = portletProperties.getActionlog().getCacheSize();
                if (cacheSize <= 0) {
                    cacheSize = ACTION_LOG_CACHE_SIZE_DEFAULT;
                }

                ValueOperations<String, List<ActionLog>> va = redisTemplate.opsForValue();

                List<ActionLog> actionLogs = va.get(ACTION_LOG_CACHE_NAME);
                if (actionLogs == null) {
                    actionLogs = Lists.newArrayList();
                }
                actionLogs.add(actionLog);
                if (actionLogs.size() >= cacheSize) {
                    saves(actionLogs);
                    redisTemplate.delete(ACTION_LOG_CACHE_NAME);
                    log.info("批量写入actionLog日志并清空缓存。数量:{}", actionLogs.size());
                } else {
                    va.set(ACTION_LOG_CACHE_NAME, actionLogs);
                }

            } else {
                save(actionLog);
                log.debug("缓存写入actionLog日志: {}", actionLog);
            }
            return actionLog;
        } catch (Exception e) {
            logger.warn("保持action日志失败:{}", e.getMessage());
        }
        return null;
    }

    protected ActionLog doMarshallActionLog(String action,
                                            String actionName,
                                            String userName,
                                            ActionChannelEnum actionChannel,
                                            String version,
                                            String comments,
                                            HttpServletRequest request) {
        ActionLog actionLog = new ActionLog();
        actionLog.setActionKey(Strings.right(action, 128));
        actionLog.setActionName(actionName);
        if (actionChannel == null && request == null) {
            throw new IllegalArgumentException("actionChannel和request不能同时为空");
        }
        if (actionChannel == null && request != null) {
            actionLog.setChannel(parseChannelForReqeust(request));
        } else {
            actionLog.setChannel(actionChannel);
        }
        actionLog.setActionUrl(Strings.right(Servlets.getRequestPath(request), 128));
        actionLog.setComments(comments);
        actionLog.setUserName(userName);
        actionLog.setChannelVersion(version);
        actionLog.setUserIp(IPUtil.getIpAddr(request));
        if (request != null) {
            actionLog.setChannelInfo(
                    Strings.substring(Servlets.getHeaderValue(request, "User-Agent"), 0, 255));
        }
        return actionLog;
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
