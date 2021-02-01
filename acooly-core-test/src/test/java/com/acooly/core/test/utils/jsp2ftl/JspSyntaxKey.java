/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-01-18 17:51
 */
package com.acooly.core.test.utils.jsp2ftl;
/**
 * 语法关键字
 *
 * @author zhangpu
 * @date 2021-01-18 17:51
 */

import com.acooly.core.utils.Strings;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum JspSyntaxKey implements Function<String, String> {
    IF("c:if", "^<c:forEach.*</c:forEach>$", "条件") {
        @Override
        public String apply(String s) {
            return null;
        }
    },
    IFQ("if", "\\$\\{([',\",\\w]*)==([',\",\\w]*)\\?([',\",\\w]*):([',\",\\w]*)\\}", "问号表达式") {
        @Override
        public String apply(String jsp) {
            String regex = "\\$\\{([',\",\\w]*)==([',\",\\w]*)\\?([',\",\\w]*):([',\",\\w]*)\\}";
            String format = "<#if %s==%s>%s<#else>%s</#if>";
            return regexConvert(jsp, regex, format);
        }
    },
    FOREACH("c:forEach", "", "FOR循环") {
        @Override
        public String apply(String jsp) {
            Jsps.JstlForEach forEach = Jsps.forEach(jsp);
            String ftl = null;
            if (forEach.getDataType() == Jsps.DataType.Map) {
                ftl = "<#list " + forEach.getItems() + " as k,v><option value=\"${k}\">${v}</option></#list>";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("<#list ").append(forEach.getItems()).append(" as ").append(forEach.getVar()).append(">");
                sb.append(forEach.getChildren());
                sb.append("</#list>");
                ftl = sb.toString();
            }
            return ftl;
        }
    };

    private final String tag;
    private final String regex;
    private final String message;

    JspSyntaxKey(String tag, String regex, String message) {
        this.tag = tag;
        this.regex = regex;
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getRegex() {
        return regex;
    }

    public String tag() {
        return tag;
    }

    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (JspSyntaxKey type : values()) {
            map.put(type.getTag(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static JspSyntaxKey find(String code) {
        for (JspSyntaxKey status : values()) {
            if (status.getTag().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<JspSyntaxKey> getAll() {
        List<JspSyntaxKey> list = new ArrayList<JspSyntaxKey>();
        for (JspSyntaxKey status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (JspSyntaxKey status : values()) {
            list.add(status.getTag());
        }
        return list;
    }

    private static String regexConvert(String text, String regex, String format) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(text);
        String result = text;
        while (matcher.find()) {
            String group = null;
            List<String> items = Lists.newArrayList();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (i > 2) {
                    items.add(Strings.replaceAll(matcher.group(i), "[',\"]+", ""));
                } else {
                    items.add(matcher.group(i));
                }
            }
            String ftlItem = String.format(format, items.toArray());
            result = p.matcher(result).replaceFirst(ftlItem);
        }
        return result;
    }

}
