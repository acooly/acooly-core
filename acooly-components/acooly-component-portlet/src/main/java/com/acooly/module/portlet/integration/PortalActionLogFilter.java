/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-21 00:48 创建
 */
package com.acooly.module.portlet.integration;

import com.acooly.core.utils.Strings;
import com.acooly.module.portlet.service.ActionLogService;
import com.acooly.module.security.utils.ShiroUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 访问日志 通用收集filter
 *
 * @author acooly
 */
@Component
public class PortalActionLogFilter implements Filter {

    private static final String SESSION_USER_KEY_PARAMETER_NAME = "sessionUserKey";

    private String sessionUserKey;

    @Resource
    private ActionLogService actionLogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configSessionUserKey = filterConfig.getInitParameter(SESSION_USER_KEY_PARAMETER_NAME);
        if (Strings.isNotBlank(configSessionUserKey)) {
            this.sessionUserKey = configSessionUserKey;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        actionLogService.logger(request, getSessionUserName(request));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    protected String getSessionUserName(HttpServletRequest request) {
       return ShiroUtils.getCurrentUser().getUsername();
    }

    @Override
    public void destroy() {

    }
}
