/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
package com.acooly.core.test.portal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
@Slf4j
@Controller
@RequestMapping("/test/httpmethod/")
public class SimplePortalTestController {


    @RequestMapping("index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        return "The request http method: " + request.getMethod();
    }


    /**
     * 未完成方案和测试
     *
     * @param request
     * @return
     */
    @RequestMapping("testSession")
    @ResponseBody
    public Object testSession(HttpServletRequest request) {


        String requestSessionId = request.getRequestedSessionId();
        log.info("client sessionId: {}", requestSessionId);
        // 让旧session失效
        request.getSession(true).invalidate();

        // 登录认证，设置新的session变量。
        HttpSession session = request.getSession(true);
        String serverSessionId = session.getId();
        log.info("server sessionId: {}", requestSessionId);
        return serverSessionId;
    }

}
