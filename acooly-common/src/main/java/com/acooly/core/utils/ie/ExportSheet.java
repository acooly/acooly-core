/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-12 17:42
 */
package com.acooly.core.utils.ie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author zhangpu
 * @date 2022-09-12 17:42
 */
@Getter
@Setter
@NoArgsConstructor
public class ExportSheet {
    /**
     * sheet名称，如果没有，则使用list成员自动生成
     */
    private String sheetName;
    @NotEmpty
    private List<?> list;

    public ExportSheet(List<?> list) {
        this.list = list;
    }

    public ExportSheet(String sheetName, List<?> list) {
        this.list = list;
        this.sheetName = sheetName;
    }
}
