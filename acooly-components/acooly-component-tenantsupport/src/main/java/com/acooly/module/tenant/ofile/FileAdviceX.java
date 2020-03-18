package com.acooly.module.tenant.ofile;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.dubbo.common.utils.StringUtils;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class FileAdviceX {

    public static final String CLASS = "com.acooly.module.ofile.OFileProperties";


    @OnMethodExit
    public static void before( @Return(typing = Typing.DYNAMIC, readOnly = false) String path ) {
        String tenantId = TenantContext.get();
        if (StringUtils.isEmpty(tenantId)) {
            throw new BusinessException("上下文中不存在tenantId，请先认证者登录");
        }

        path = path + tenantId ;
    }

}
