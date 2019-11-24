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

import com.acooly.core.utils.Servlets;
import com.acooly.module.defence.exception.AccessDeniedException;
import com.acooly.module.defence.exception.AccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 防御异常处理
 *
 * @author zhangpu 调整异常输出
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        if (!response.isCommitted()) {
            log.error("防御 异常，url: {}, Exception: {}", Servlets.getRequestPath(request), accessDeniedException.getClass().getSimpleName());
            if (this.errorPage != null) {
                request.setAttribute("SPRING_SECURITY_403_EXCEPTION", accessDeniedException);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Servlets.forward(request, response, this.errorPage);
            } else {
                response.setCharacterEncoding("UTF-8");
                response.sendError(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
            }
        }
    }

    public void setErrorPage(String errorPage) {
        if (errorPage != null && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with \'/\'");
        } else {
            this.errorPage = errorPage;
        }
    }
}
