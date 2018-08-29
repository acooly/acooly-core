package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Money;
import org.springframework.core.convert.converter.Converter;

/**
 * 字符串元转换为Money对象
 *
 * @author zhangpu
 * @date 2018-08-22
 */
public class StringYuanToMoneyConverter implements Converter<String, Money> {
    @Override
    public Money convert(String source) {
        try {
            return Money.amout(source);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
