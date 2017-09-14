package com.acooly.module.web.formatter;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.Strings;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author qiubo@yiji.com
 */
public class MoneyFormatter implements Formatter<Money> {
    @Override
    public Money parse(String text, Locale locale) throws ParseException {
        if (Strings.isEmpty(text)) {
            return null;
        } else {
            return new Money(text);
        }
    }

    @Override
    public String print(Money object, Locale locale) {
        if (object == null) {
            return null;
        }
        return object.getAmount().toString();
    }
}

