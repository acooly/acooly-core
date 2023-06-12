/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-07 20:57
 */
package com.acooly.core.utils.ie;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ie.anno.ExportStyle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhangpu
 * @date 2022-08-07 20:57
 */
@Getter
@Setter
@NoArgsConstructor
public class ExportStyleMeta {

    private String fontName;

    private short fontSize = -1;

    private boolean fontBold;

    private String backgroundColor;

    private short rowHeight = -1;

    public ExportStyleMeta(ExportStyle exportStyle) {
        this.fontName = exportStyle.fontName();
        this.fontSize = exportStyle.fontSize();
        this.fontBold = exportStyle.fontBold();
        this.backgroundColor = exportStyle.backgroundColor();
        this.rowHeight = exportStyle.rowHeight();
    }

    public byte[] getRgb() {
        if (Strings.isBlank(this.getBackgroundColor())) {
            return null;
        }
        return Exports.hex2RGB(this.backgroundColor);
    }

    /**
     * 是否需要设置字体
     *
     * @return true:已设置字体,false:未设置字体
     */
    public boolean requireFont() {
        return this.fontBold || Strings.isNotBlank(fontName) || this.fontSize != -1;
    }
}
