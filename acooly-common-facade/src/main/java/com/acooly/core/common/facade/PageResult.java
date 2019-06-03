/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-07 17:55 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.enums.ResultStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubo@yiji.com
 * @author zhangpu on 2019-06-03: from方法存在无法强制向下转型的问题(保留在OpenApi服务层PageApiResponse使用from做转换)，新增with方法代替
 */
@Getter
@Setter
public class PageResult<T> extends ResultBase implements DtoAble {
    private PageInfo<T> dto;

    public static <T> PageResult<T> from(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setDto(pageInfo);
        result.setStatus(ResultStatus.success);
        return result;
    }

    /**
     * 把T类型PageInfo转换为S类型PageInfo后构造结果对象
     */
    public static <T, S> PageResult<S> from(PageInfo<T> pageInfo, Class<S> clazz) {
        PageResult<S> result = new PageResult<>();
        if (pageInfo != null) {
            result.setDto(pageInfo.to(clazz));
        }
        result.setStatus(ResultStatus.success);
        return result;
    }

    public static <R extends PageResult<S>, S> R with(PageInfo<S> pageInfo, Class<R> clazz) {
        R result = null;
        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
        result.setDto(pageInfo);
        result.setStatus(ResultStatus.success);
        return result;
    }

    public static <R extends PageResult<S>, T, S> R with(PageInfo<T> pageInfo, Class<R> clazz, PageInfo.Function<T, S> function) {
        R result = null;
        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
        result.setDto(pageInfo.to(function));
        result.setStatus(ResultStatus.success);
        return result;
    }

}
