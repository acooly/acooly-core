/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 20:09
 */
package com.acooly.core.common.domain;

/**
 * 标记可排序
 * <p>
 * 提供上移和置顶功能，约定要求entity必须提供sortTime属性
 *
 * @author zhangpu
 * @date 2019-09-01 20:09
 */
public interface Sortable {

    /**
     * 获取排序值
     *
     * @return
     */
    Long getSortTime();

    /**
     * 设置排序值
     * @param sortTime
     */
    void setSortTime(Long sortTime);

}
