/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-12 17:55
 */
package com.acooly.core.utils.ie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangpu
 * @date 2022-09-12 17:55
 */
@Getter
@Setter
@NoArgsConstructor
public class ExportSheetMeta extends ExportSheet {

    private ExportModelMeta exportModelMeta;

    public ExportSheetMeta(List<?> list, ExportModelMeta exportModelMeta) {
        super(list);
        this.exportModelMeta = exportModelMeta;
    }

    public ExportSheetMeta(String sheetName, List<?> list, ExportModelMeta exportModelMeta) {
        super(sheetName, list);
        this.exportModelMeta = exportModelMeta;
    }
}
