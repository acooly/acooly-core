package com.acooly.module.tenant.security;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.StringUtils;
import com.acooly.module.tenant.core.TenantContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class CheckAdviceX {

    public static final String CLASS = "com.acooly.module.security.shiro.filter.UrlResourceAuthorizationFilter";


    @OnMethodExit
    public static void after( @Return(typing = Typing.DYNAMIC) boolean result,
            @Argument(value = 1, typing = Typing.DYNAMIC)
                    ServletRequest request ) {
        if (result == true) {
            HttpSession session = ( (HttpServletRequest) request ).getSession();
            String tenantId = (String) session.getAttribute("TENANT_ID");
            if (StringUtils.isEmpty(tenantId)) {
                // 这种情况几乎不可能发生
                throw new BusinessException("session中不存在 tenantId");
            }
            TenantContext.set(tenantId);
        }

    }
}
