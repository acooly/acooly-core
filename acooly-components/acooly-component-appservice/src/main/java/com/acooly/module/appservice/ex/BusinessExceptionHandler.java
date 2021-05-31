/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * lixiang@yiji.com 2014-12-03 17:47 创建
 *
 */
package com.acooly.module.appservice.ex;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.enums.ResultStatus;

/**
 * BusinessException拦截处理
 *
 * @author zhangpu
 */
public class BusinessExceptionHandler implements ExceptionHandler<BusinessException> {

    @Override
    public void handle(ExceptionContext<?> context, BusinessException e) {
        ResultBase res = context.getResponse();
        res.setStatus(e.getErrorCode(), e.getDetail());
        // 兼容老的BusinessException构造时，ErrorCode为被正常赋值，不是真的内部异常，保持原有逻辑
        if (res.getCode() != null && !e.getErrorCode().code().equals(e.getCode())) {
            res.setStatus(ResultStatus.failure);
            res.setCode(e.code());
            res.setMessage(e.message() == null ? e.getDetail() : e.getMessage());
            res.setDetail(e.getDetail() == null ? e.getMessage() : e.getDetail());
        }
    }
}
