/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-12 17:41
 */
package com.acooly.core.utils.ie;

import com.acooly.core.common.facade.InfoBase;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 导出文件请求
 *
 * @author zhangpu
 * @date 2022-09-12 17:41
 */
@Getter
@Setter
@NoArgsConstructor
public class ExportOrder extends InfoBase {

    /**
     * 输出的文件路径
     * 必须是文件，注意权限
     */
    @NotBlank
    private String outputFilePath;

    /**
     * 导出的数据页, Excel的sheet
     */
    @NotEmpty
    private List<ExportSheet> exportSheets = Lists.newArrayList();

    public ExportOrder(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public ExportOrder(List<ExportSheet> exportSheets, String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.exportSheets = exportSheets;
    }

    public ExportOrder(ExportSheet exportSheet, String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.exportSheets.add(exportSheet);
    }

    public ExportOrder(String sheetName, List<?> list, String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.exportSheets.add(new ExportSheet(sheetName, list));
    }

    public void addExportSheet(String sheetName, List<?> list) {
        this.exportSheets.add(new ExportSheet(sheetName, list));
    }

    public void addExportSheet(List<?> list) {
        this.exportSheets.add(new ExportSheet(null, list));
    }
}
