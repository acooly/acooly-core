/**
 * create by zhangpu date:2015年9月28日
 */
package com.acooly.core.common.web.support;

import com.acooly.core.utils.Dates;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 多种日期时间格式自适应 PropertyEditor 实现
 *
 * @author zhangpu
 * @date 2015年9月28日
 */
public class MultiFormatCustomDateEditor extends PropertyEditorSupport {

    private String formatPattern = Dates.CHINESE_DATETIME_FORMAT_LINE;

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? Dates.format(value, formatPattern) : "");
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            try {
                setValue(Dates.parse(text));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }
}
