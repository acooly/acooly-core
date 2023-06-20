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
 * 正则表达式工具类
 *
 * @author zhangpu
 * @date 2019-09-01 17:20
 */
@Slf4j
public class Regexs {

    /**
     * 整个匹配group(0)
     */
    private static final int REGEX_GROUP_WHOLE = 0;

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
        return finder(p, text, REGEX_GROUP_WHOLE);
    }

    /**
     * 正则匹配查找
     *
     * @param p          正则对象
     * @param text       被查对象
     * @param groupIndex 正则分组（0:全匹配，1：第一个括号...）,默认为：0
     * @return 返回匹配的多个模式内容中的${groupIndex}第个
     */
    public static String finder(Pattern p, String text, Integer groupIndex) {
        List<String> result = finderWholes(p, text);
        if (Collections3.isEmpty(result)) {
            return null;
        }
        if (groupIndex == null) {
            groupIndex = REGEX_GROUP_WHOLE;
        }
        if (groupIndex < result.size()) {
            return result.get(groupIndex);
        }
        return null;
    }

    /**
     * 查找所有匹配的模式内容
     * <p>
     * 匹配所有的模式内容，包括分组。例如：
     * <p>
     * regex：(//d{4})-(//d{2})-(//d{2})
     * text：date1:2022-09-02,date2:2022-09-03...
     * 匹配返回8个结果：2022-09-02,2022,09,02,2022-09-03,2022,09,03
     *
     * @param p    正则对象
     * @param text 被查对象
     * @return 匹配的模式内容列表
     */
    public static List<String> finders(Pattern p, String text) {
        Matcher matcher = p.matcher(text);
        List<String> result = Lists.newArrayList();
        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        }
        return Collections3.isEmpty(result) ? null : result;
    }

    /**
     * 查找所有匹配的模式内容
     * <p>
     * 匹配所有的完整匹配模式内容，不包括分组。例如：
     * <p>
     * regex：(//d{4})-(//d{2})-(//d{2})
     * text：date1:2022-09-02,date2:2022-09-03...
     * 匹配返回2个结果：2022-09-02,2022-09-03
     *
     * @param p    正则对象
     * @param text 被查对象
     * @return 匹配的模式内容列表
     */
    public static List<String> finderWholes(Pattern p, String text) {
        Matcher matcher = p.matcher(text);
        List<String> result = Lists.newArrayList();
        while (matcher.find()) {
            result.add(matcher.group(REGEX_GROUP_WHOLE));
        }
        return Collections3.isEmpty(result) ? null : result;
    }


    public static String finder(CommonRegex commonRegex, String text) {
        return finder(commonRegex.pattern, text);
    }

    public static List<String> finders(CommonRegex commonRegex, String text) {
        return finderWholes(commonRegex.pattern, text);
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
