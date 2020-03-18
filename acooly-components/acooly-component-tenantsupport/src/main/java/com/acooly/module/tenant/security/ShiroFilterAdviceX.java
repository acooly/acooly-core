package com.acooly.module.tenant.security;

import com.acooly.core.utils.StringUtils;
import com.acooly.module.tenant.core.TenantContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.asm.Advice.Argument;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class ShiroFilterAdviceX {

    public static final String CLASS = "org.apache.shiro.web.servlet.ProxiedFilterChain";


    @OnMethodEnter
    public static void before(
            @Argument(value = 0, typing = Typing.DYNAMIC)
                    ServletRequest request ) {
        HttpSession session = ( (ShiroHttpServletRequest) request ).getSession();
        String tenantId = (String) session.getAttribute("TENANT_ID");
        if (!StringUtils.isEmpty(tenantId)) {
            TenantContext.set(tenantId);
        }

    }
}
