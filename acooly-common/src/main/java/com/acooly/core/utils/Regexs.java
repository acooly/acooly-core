/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 17:20
 */
package com.acooly.core.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangpu
 * @date 2019-09-01 17:20
 */
@Slf4j
public class Regexs {


    private static final int REGEX_GOURP_FIRST = 1;

    /**
     * 判断正则匹配
     *
     * @param regex 正则表达式
     * @param value 被判断的字符串
     * @return 是否匹配（true:匹配）
     */
    public static boolean matcher(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(value).matches();
    }

    public static String finder(String regex, String text) {
        Pattern p = Pattern.compile(regex);
        return finder(p, text);
    }

    public static String finder(Pattern p, String text) {
        Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            return matcher.group(REGEX_GOURP_FIRST);
        }
        return null;
    }

    public static List<String> finders(Pattern p, String text) {
        Matcher matcher = p.matcher(text);
        List<String> result = Lists.newArrayList();
        while (matcher.find()) {
            result.add(matcher.group(REGEX_GOURP_FIRST));
        }
        return result;
    }

    public static String finder(CommonRegex commonRegex, String text) {
        return finder(commonRegex.pattern, text);
    }

    public static List<String> finders(CommonRegex commonRegex, String text) {
        return finders(commonRegex.pattern, text);
    }

    /**
     * 常用正则收集
     */
    public static enum CommonRegex {

        /**
         * 引号
         */
        QUITATION("QUITATION", "引号", Pattern.compile("['|\"](.*?)['|\"]"));

        final String code;
        final String name;
        final Pattern pattern;

        CommonRegex(String code, String name, Pattern pattern) {
            this.code = code;
            this.name = name;
            this.pattern = pattern;
        }
    }


}
