package com.acooly.module.tenant.security;

import com.acooly.core.utils.StringUtils;
import com.acooly.module.tenant.core.TenantContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class IndexAdviceX {


    public static final String CLASS = "com.acooly.module.security.web.ManagerController";


    @OnMethodEnter
    public static void before(
            @Argument(value = 0, typing = Typing.DYNAMIC) HttpServletRequest request ) {
        HttpSession session = request.getSession();
        String tenantId = (String) session.getAttribute("TENANT_ID");
        if (!StringUtils.isEmpty(tenantId)) {
            TenantContext.set(tenantId);
        }
    }
}
