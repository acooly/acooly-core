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
public class ExportModelMeta extends InfoBase {


    /**
     * 文件名
     */
    private String fileName;

    /**
     * 标题行样式
     */
    private ExportStyleMeta headerStyleMeta;

    /**
     * 全局通用行高
     */
    private short rowHeight;

    /**
     * 全局是否有边框
     */
    private boolean border;

    /**
     * 属性列表
     */
    private List<ExportColumnMeta> items;

    /**
     * 表头
     */
    private List<String> headers;

    /**
     * 行数据
     */
    private List<Object> row;

    public ExportColumnMeta getExportItem(int index) {
        return items.get(index);
    }

}
