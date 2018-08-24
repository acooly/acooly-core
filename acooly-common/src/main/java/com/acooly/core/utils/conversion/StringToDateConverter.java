package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Dates;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 字符串元转换为Money对象
 *
 * @author zhangpu
 * @date 2018-08-22
 */
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        try {
            return Dates.parse(source);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
