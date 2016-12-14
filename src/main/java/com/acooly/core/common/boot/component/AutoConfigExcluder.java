/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:08 创建
 */
package com.acooly.core.common.boot.component;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public interface AutoConfigExcluder {
    /**
     * 返回需要排除的autoconfig class 类名
     * @return 列表
     */
    default List<String> excludeAutoconfigClassNames() {
        return Lists.newArrayList();
    }
}
