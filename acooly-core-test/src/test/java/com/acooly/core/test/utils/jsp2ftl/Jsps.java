/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-12-31 15:34
 */
package com.acooly.core.test.utils.jsp2ftl;

import com.acooly.core.utils.Regexs;
import com.acooly.core.utils.Strings;
import com.google.common.base.MoreObjects;
import jodd.jerry.Jerry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangpu
 * @date 2020-12-31 15:34
 */
@Slf4j
public class Jsps {


    /**
     * <c:forEach items="${allCustomerTypes}" var="e">
     * <option value="${e.key}">${e.value}</option>
     * </c:forEach>
     * <p>
     * <#list allStatuss as k,v>
     * <option value="${k}">${v}</option>
     * </#list>
     *
     * @param jstl
     * @return
     */
    public static JstlForEach forEach(String jstl) {
//        String regex = JspSyntaxKey.forEach.getRegex();
//        String forEach = Regexs.finder(regex, jstl);
        Jerry jerry = Jerry.of(jstl);
        JstlForEach jstlForEach = new JstlForEach();
        jstlForEach.setItems(getVariable(jerry.contents().attr("items")));
        jstlForEach.setVar(jerry.contents().attr("var"));
        jstlForEach.setChildren(jerry.children().html());
        Iterator<Jerry> options = jerry.children().iterator();
        while (options.hasNext()) {
            Jerry option = options.next();
            if (!Strings.contains(option.contents().html(), "$")) {
                continue;
            }
            String value = getVariable(option.contents().attr("value"));
            String text = getVariable(option.get(0).getTextContent());
            if (Strings.equalsIgnoreCase(value, jstlForEach.var + ".key")
                    && Strings.equalsIgnoreCase(text, jstlForEach.var + ".value")) {
                jstlForEach.dataType = DataType.Map;
                break;
            }
        }
        return jstlForEach;
    }

    public static String getVariable(String script) {
        String regex = "\\$\\{([\\.,[,],\\w]*)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(script);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Data
    public static class JstlForEach {
        private String items;
        private DataType dataType;
        private String var;
        private String children;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("items", items)
                    .add("dataType", dataType)
                    .add("var", var)
                    .toString();
        }
    }

    public static enum DataType {
        Map, Object
    }

}
