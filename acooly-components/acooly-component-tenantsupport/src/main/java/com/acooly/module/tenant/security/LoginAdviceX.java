package com.acooly.module.tenant.security;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.dubbo.common.utils.StringUtils;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */

public class LoginAdviceX {

    public static final String CLASS = "com.acooly.module.security.shiro.filter.CaptchaFormAuthenticationFilter";

    @OnMethodEnter
    public static void before(
            @Argument(value = 1, typing = Typing.DYNAMIC) ServletRequest request ) {
        String tenantId = request.getParameter("tenantId");
        if (StringUtils.isEmpty(tenantId)) {
            throw new BusinessException("登录请求中不存在 tenantId ");
        }
        if (tenantId.length() > 32) {
            throw new BusinessException("tenantId: " + tenantId + " 格式长度不对");
        }
        TenantContext.set(tenantId);
    }

    @OnMethodExit
    public static void before( @Argument(value = 1, typing = Typing.DYNAMIC) ServletRequest request,
            @Return(typing = Typing.DYNAMIC) boolean result ) {
        if(result == true){
            HttpSession session = ((HttpServletRequest)request).getSession();
            session.setAttribute("TENANT_ID",TenantContext.get());
        }
    }
}
