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
 * 导出模型元数据
 *
 * @author zhangpu
 * @date 2022-08-03 13:52
 */
@Getter
@Setter
public class ExportModelMeta extends InfoBase {


    /**
     * 文件名或sheet名称
     */
    private String name;

    /**
     * 实体类
     */
    private Class<?> clazz;

    /**
     * 是否显示表头
     */
    private boolean headerShow;

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

    /**
     * 临时对象
     */
    private Object style;

    public ExportColumnMeta getExportItem(int index) {
        return items.get(index);
    }

}
