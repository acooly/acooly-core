package com.acooly.module.tenant.ofile;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.tenant.core.TenantContext;
import com.acooly.module.tenant.core.TenantMessage;
import com.alibaba.dubbo.common.utils.StringUtils;
import java.io.File;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import net.engio.mbassy.bus.MessagePublication;
import org.slf4j.MDC;

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
        path = path + tenantId + File.separator;
    }

}
