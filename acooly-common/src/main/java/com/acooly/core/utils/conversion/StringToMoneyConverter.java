package com.acooly.core.utils.conversion;

import com.acooly.core.utils.Money;
import org.springframework.core.convert.converter.Converter;

public class StringToMoneyConverter implements Converter<String, Money> {
    @Override
    public Money convert(String source) {
        Money m = new Money();
        m.setCent(Long.parseLong(source));
        return m;
    }
}
