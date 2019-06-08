/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-08 10:42
 */
package com.acooly.core.common.exception;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import lombok.extern.slf4j.Slf4j;

/**
 * 专用参数异常
 * 三元异常,格式化错误消息
 *
 * @author zhangpu
 * @date 2019-06-08 10:42
 */
@Slf4j
public class ParameterException extends BusinessException {

    public static final String STRING_FORMAT_PATTERN = "%s";

    private Messageable messageable;
    private String paramName;
    private String detail;

    public ParameterException() {
        super();
    }

    public ParameterException(String detail) {
        this(null, detail);
    }

    public ParameterException(String paramName, String detail) {
        this(CommonErrorCodes.PARAMETER_ERROR, paramName, detail);
    }

    public ParameterException(Messageable messageable, String paramName, String detail) {
        this.messageable = CommonErrorCodes.PARAMETER_ERROR;
        this.paramName = paramName;
        this.detail = String.format(detail, Strings.trimToEmpty(this.paramName));
    }

    @Override
    public String code() {
        return super.code();
    }

    @Override
    public String message() {
        return super.message();
    }

    public String getParamName() {
        return paramName;
    }

    public String getDetail() {
        return detail;
    }


    @Override
    public String getMessage() {
        return this.detail;
    }
}
