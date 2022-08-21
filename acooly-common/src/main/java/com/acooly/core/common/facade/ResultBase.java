/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.mapper.BeanCopier;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

/**
 * Facade返回对象基类
 * 注意：从5.0.0-SNAPSHOT重构简化为：
 * 1、code/message/detail三元字符串返回
 * 2、去除本身的Map类型继承关系，在DEBUG时能清楚的暂时所有子属性。
 * 3、去除Messageable的status属性，解决判断混乱，同时支持feign序列化对接口类型序列化麻烦问题。
 *
 * @author zhangpu
 */
@Getter
@Setter
public class ResultBase extends LinkedHashMapParameterize<String, Object> implements Resultable {

    /**
     * 消息码
     */
    private String code = ResultCode.SUCCESS.getCode();
    /**
     * 消息串
     */
    private String message = ResultCode.SUCCESS.getMessage();
    /**
     * 消息详细信息
     */
    private String detail;


    /**
     * 快速构建result对象
     *
     * @param tClass
     * @param status
     * @param <T>
     * @return
     */
    public static <T extends ResultBase> T of(Class<T> tClass, Messageable status) {
        return of(tClass, status, null);
    }

    /**
     * 快速构建result对象
     *
     * @param tClass
     * @param status
     * @param detail
     * @param <T>
     * @return
     */
    public static <T extends ResultBase> T of(Class<T> tClass, Messageable status, String detail) {
        T t = Reflections.createObject(tClass);
        t.makeResult(status, detail);
        return t;
    }

    /**
     * 通过Messageable对象设置结果
     *
     * @param status
     */
    public void makeResult(Messageable status) {
        makeResult(status, null);
    }

    public void makeResult(Messageable status, String detail) {
        this.code = status.code();
        this.message = status.message();
        if (Strings.isNotBlank(detail)) {
            this.detail = detail;
        }
    }

    public void markProcessing() {
        this.code = ResultCode.PROCESSING.code();
        this.message = ResultCode.PROCESSING.message();
    }

    /**
     * 判断成功
     *
     * @return
     */
    public boolean success() {
        return Strings.equalsIgnoreCase(code(), ResultCode.SUCCESS.code());
    }

    /**
     * 判断处理中
     *
     * @return
     */
    public boolean processing() {
        return Strings.equalsIgnoreCase(code(), ResultCode.PROCESSING.code());
    }

    /**
     * 判断失败
     *
     * @return
     */
    public boolean failure() {
        return !success() && !processing();
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
        return this.message;
    }

    public String detail() {
        return detail;
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
     * 失败时跑出异常
     *
     * @return
     */
    public ResultBase throwIfFailure() {
        if (!success() && !processing()) {
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
}
