/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;

/**
 * Facade基础返回参数
 *
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

    private String message = ResultCode.SUCCESS.getMessage();

    private String detail = ResultCode.SUCCESS.getMessage();


    public void setStatus(Messageable status) {
        this.status = status;
        this.code = status.code();
        this.message = status.message();
        // 保留status.message()作为默认的detail，兼容老代码
        this.detail = status.message();
    }

    public void setStatus(Messageable status, String detail) {
        this.status = status;
        this.code = status.code();
        this.message = status.message();
        this.detail = this.detail;
    }

    public void markProcessing() {
        this.status = ResultStatus.processing;
        this.code = ResultCode.PROCESSING.code();
        this.detail = ResultCode.PROCESSING.message();
    }

    @Override
    public Messageable getStatus() {
        return status;
    }

    @Override
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
        return status == ResultStatus.success || Strings.equalsIgnoreCase(status.code(), ResultCode.SUCCESS.code());
    }

    public boolean processing() {
        return status == ResultStatus.processing || Strings.equalsIgnoreCase(status.code(), ResultCode.PROCESSING.code());
    }

    public boolean failure() {
        return status == ResultStatus.failure || Strings.equalsIgnoreCase(status.code(), ResultCode.FAILURE.code());
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

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 当status != ResultStatus.success抛出业务异常
     */
    public ResultBase throwIfNotSuccess() {
        if (!success()) {
            throw new BusinessException(this.code(), this.message(), this.getDetail());
        }
        return this;
    }


    /**
     * 当status == ResultStatus.failure抛出业务异常
     */
    public ResultBase throwIfFailure() {
        if (status == ResultStatus.failure) {
            throw new BusinessException(this.code(), this.message(), this.getDetail());
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

    //jdk 1.7打包兼容1.8 function interface
    public interface Consumer<T> {
        void accept(T t);
    }

}
