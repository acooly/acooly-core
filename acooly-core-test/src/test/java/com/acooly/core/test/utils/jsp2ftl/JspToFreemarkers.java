/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-01-13 11:50
 */
package com.acooly.core.test.utils.jsp2ftl;

import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSP(JSTL)转FTL工具
 *
 * @author zhangpu
 * @date 2021-01-13 11:50
 */
@Slf4j
public class JspToFreemarkers {

    public static void convert() {
        String sourceFile = "/Users/zhangpu/workspace/acooly/v5/acooly-showcase/acooly-showcase-core/" +
                "src/main/resources/META-INF/resources/WEB-INF/jsp/manage/showcase/customerEdit.jsp";
        String source = null;
        try {
            source = FileUtils.readFileToString(new File(sourceFile), "UTF-8");
        } catch (Exception e) {
        }
        doPreConvert(source);
        source = ftlQExp(source);


    }


    protected static void doPreConvert(String source) {
        source = Strings.replaceAll(source, "\\$\\{pageContext.request.contextPath\\}", "");
        source = Strings.replaceAll(source, "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>", "");
        source = Strings.replaceAll(source, "<%@ page contentType=\"text/html;charset=UTF-8\" %>", "");
        source = Strings.replaceAll(source, "<%@ include file=\"/WEB-INF/jsp/manage/common/taglibs.jsp\" %>", "<#assign jodd=JspTaglibs[\"http://www.springside.org.cn/jodd_form\"] />");
        source = Strings.replaceAll(source, "jodd:form", "@jodd.form");
    }


    /**
     * JSP问号表达式转FTL
     *
     * @param jsp
     * @return
     */
    private static String ftlQExp(String jsp) {
        String regex = "\\$\\{([',\",\\w]*)==([',\",\\w]*)\\?([',\",\\w]*):([',\",\\w]*)\\}";
        String format = "<#if %s==%s>%s<#else>%s</#if>";
        return regexConvert(jsp, regex, format);
    }

    public static String forEachToList(String jsp) {
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


    public static void forEachConvert(String source, String tag, Function<String, String> function) {
        String open = "<" + tag;
        String close = "</" + tag + ">";
        final int strLen = source.length();
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        int pos = 0;
        int end = 0;
        while (pos < strLen - closeLen) {
            int start = source.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            if (start > pos ) {
                result.append(source.substring(pos, start));
            }
            start += openLen;
            end = source.indexOf(close, start);
            if (end < 0) {
                break;
            }
            String line = open + source.substring(start, end) + close;
            result.append(function.apply(line));
            pos = end + closeLen;
        }

        if (strLen > pos) {
            result.append(source.substring(pos, strLen));
        }

        log.info("result:{}", result);
    }

}
