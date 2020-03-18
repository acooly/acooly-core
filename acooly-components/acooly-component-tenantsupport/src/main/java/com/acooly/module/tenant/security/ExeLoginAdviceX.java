package com.acooly.module.tenant.security;

import com.acooly.module.tenant.core.TenantContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class ExeLoginAdviceX {


    public static final String CLASS = "com.acooly.module.security.web.ManagerController";


    @OnMethodExit
    public static void after(
            @Argument(value = 0, typing = Typing.DYNAMIC) HttpServletRequest request ) {
        HttpSession session = request.getSession();
        session.setAttribute("TENANT_FLAG",TenantContext.getAllTenantId());
    }

}
