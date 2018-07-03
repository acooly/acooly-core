package com.acooly.module.web.formatter;

import com.acooly.core.common.type.DBMap;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-02 17:01
 */
public class DBMapFormatter implements Formatter<DBMap> {
    @Override
    public DBMap parse(String text, Locale locale) throws ParseException {
        return DBMap.fromJson(text);
    }

    @Override
    public String print(DBMap object, Locale locale) {
        return object.toJson();
    }
}
