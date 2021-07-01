/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-07-01 00:12
 */
package com.acooly.module.web.formatter;

import com.acooly.core.utils.BigMoney;
import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * BigMoney的Web序列化
 *
 * @author zhangpu
 * @date 2021-07-01 00:12
 */
@Slf4j
public class BigMoneyFormatter implements Formatter<BigMoney> {

    @Override
    public BigMoney parse(String text, Locale locale) throws ParseException {
        if (Strings.isEmpty(text) || !Strings.isNumber(text)) {
            return null;
        }
        return BigMoney.valueOf(text);
    }

    @Override
    public String print(BigMoney object, Locale locale) {
        return object.toString();
    }
}
