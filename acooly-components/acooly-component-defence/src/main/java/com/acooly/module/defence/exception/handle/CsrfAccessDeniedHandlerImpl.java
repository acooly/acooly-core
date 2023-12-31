/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-06-25 11:26 创建
 *
 */
package com.acooly.module.defence.exception.handle;

import com.acooly.module.defence.exception.AccessDeniedException;
import com.acooly.module.defence.exception.AccessDeniedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiubo
 * @author zhangpu 调整异常输出
 */
public class CsrfAccessDeniedHandlerImpl implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CsrfAccessDeniedHandlerImpl.class);

    private String errorPage;

    public CsrfAccessDeniedHandlerImpl() {
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        if (!response.isCommitted()) {
            logger.error("CSRF防御 异常，url: {}, Exception: {}", getRequestUrl(request), accessDeniedException.getClass().getSimpleName());
            if (this.errorPage != null) {
                request.setAttribute("SPRING_SECURITY_403_EXCEPTION", accessDeniedException);
                response.setStatus(403);
                RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPage);
                dispatcher.forward(request, response);
            } else {
                response.sendError(403, accessDeniedException.getMessage());
            }
        }
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURI()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }

    public void setErrorPage(String errorPage) {
        if (errorPage != null && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with \'/\'");
        } else {
            this.errorPage = errorPage;
        }
    }
}
