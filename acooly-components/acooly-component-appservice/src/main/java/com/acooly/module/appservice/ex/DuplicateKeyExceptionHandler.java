package com.acooly.module.appservice.ex;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.common.facade.ResultCode;
import org.springframework.dao.DuplicateKeyException;

/**
 * @author qiubo@yiji.com
 */
public class DuplicateKeyExceptionHandler implements ExceptionHandler<DuplicateKeyException> {

    @Override
    public void handle(ExceptionContext<?> context, DuplicateKeyException e) {
        ResultBase res = context.getResponse();
        res.makeResult(ResultCode.DATA_UNIQUE_CONFLICT);
    }
}
