/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;

import java.util.function.Consumer;

/**
 * @author zhangpu
 */
public class ResultBase extends LinkedHashMapParameterize<String, Object>
        implements Resultable, Messageable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8702480923545642017L;

    private Messageable status = ResultStatus.success;

    /**
     * 参考 {@link ResultCode}
     */
    private String code = ResultCode.SUCCESS.getCode();

    private String detail = ResultCode.SUCCESS.getMessage();

    public Messageable getStatus() {
        return status;
    }

    public void setStatus(Messageable status) {
        this.status = status;
    }

    public void markProcessing() {
        this.status = ResultStatus.processing;
        this.code = ResultStatus.processing.code();
        this.detail = ResultStatus.processing.message();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean success() {
        return status == ResultStatus.success;
    }

    public boolean processing() {
        return status == ResultStatus.processing;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return detail;
    }

    /**
     * 当status != ResultStatus.success抛出业务异常
     */
    public ResultBase throwExceptionIfNotSuccess() {
        if (!success()) {
            throw new BusinessException(this.getDetail(), this.getCode());
        }
        return this;
    }

    /**
     * 当status != ResultStatus.success抛出业务异常
     */
    public ResultBase throwIfNotSuccess() {
        if (!success()) {
            throw new BusinessException(this.getDetail(), this.getCode());
        }
        return this;
    }

    /**
     * 当status == ResultStatus.failure抛出业务异常
     */
    public ResultBase throwExceptionIfFailure() {
        if (status == ResultStatus.failure) {
            throw new BusinessException(this.getDetail(), this.getCode());
        }
        return this;
    }

    /**
     * 当status == ResultStatus.failure抛出业务异常
     */
    public ResultBase throwIfFailure() {
        if (status == ResultStatus.failure) {
            throw new BusinessException(this.getDetail(), this.getCode());
        }
        return this;
    }

    public ResultBase ifProcessing(Consumer<ResultBase> consumer) {
        if (processing()) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     * 把当前结果对象属性复制到target对象
     *
     * @param target          目标对象
     * @param ignorePropeties 忽略参数名
     */
    public ResultBase copyTo(Object target, String... ignorePropeties) {
        BeanCopier.copy(
                this,
                target,
                BeanCopier.CopyStrategy.IGNORE_NULL,
                BeanCopier.NoMatchingRule.IGNORE,
                ignorePropeties);
        return this;
    }
}
