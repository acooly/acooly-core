/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 13:52
 */
package com.acooly.core.utils.ie;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangpu
 * @date 2022-08-03 13:52
 */
@Getter
@Setter
public class ExportResult extends InfoBase {


    /**
     * 文件名
     */
    private String fileName;

    /**
     * 属性列表
     */
    private List<ExportItem> items;

    /**
     * 表头
     */
    private List<String> titles;

    /**
     * 行数据
     */
    private List<Object> row;

}
