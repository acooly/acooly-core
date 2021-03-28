/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:16 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;

import java.util.function.BiFunction;

/**
 * @author qiubo@yiji.com
 * @author zhangpu
 */
public class SingleResult<T> extends ResultBase implements DtoAble {
    private T dto;

    public static <T> SingleResult<T> fromProcessing(T dto) {
        SingleResult<T> singleResult = new SingleResult<>();
        singleResult.setDto(dto);
        singleResult.markProcessing();
        return singleResult;
    }


    /**
     * 把T类型包装为Result对象
     *
     * @param dto
     * @param <T>
     * @return
     */
    public static <T> SingleResult<T> from(T dto) {
        SingleResult<T> singleResult = new SingleResult<>();
        singleResult.setDto(dto);
        singleResult.setStatus(ResultStatus.success);
        return singleResult;
    }

    /**
     * 把T类型转换为S类型后构造结果对象
     */
    public static <T, S> SingleResult<S> from(T t, Class<S> clazz) {
        return from(t, clazz, null);
    }

    /**
     * 把T类型转换为S类型后构造结果对象后，支持对S的后置处理BiFunction
     * <p>
     * 注意后置处理是BiFunction函数，传入T,S后，进行后置人工处理（比如转换枚举为中文说明字段等）,然后仍然返回S对象。例如：
     * <p>
     * from(project, ProjectInfo.class, (Project p, ProjectInfo projectInfo) -> {
     * // 注意：这里的projectInfo是已经完成Bean Copy处理后的对象
     * return doManualConvert(p, projectInfo);
     * });
     *
     * @param t
     * @param clazz
     * @param postProcess
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> SingleResult<S> from(T t, Class<S> clazz, BiFunction<T, S, S> postProcess) {
        SingleResult<S> singleResult = new SingleResult<>();
        if (t != null) {
            S s = BeanCopier.copy(t, clazz, BeanCopier.CopyStrategy.IGNORE_NULL);
            if (postProcess != null) {
                s = postProcess.apply(t, s);
            }
            singleResult.setDto(s);
        }
        singleResult.setStatus(ResultStatus.success);
        return singleResult;
    }

    @Override
    public T getDto() {
        return dto;
    }

    public void setDto(T dto) {
        this.dto = dto;
    }
}
