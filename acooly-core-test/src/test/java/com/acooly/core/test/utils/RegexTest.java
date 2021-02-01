/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 17:35
 */
package com.acooly.core.test.utils;

import com.acooly.core.test.utils.jsp2ftl.JspToFreemarkers;
import com.acooly.core.test.utils.jsp2ftl.Jsps;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import jodd.jerry.Jerry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangpu
 * @date 2019-09-01 17:35
 */
@Slf4j
public class RegexTest {


    @Test
    public void testMatch() throws Exception {
        String sourceFile = "/Users/zhangpu/workspace/acooly/v5/acooly-showcase/acooly-showcase-core/" +
                "src/main/resources/META-INF/resources/WEB-INF/jsp/manage/showcase/customerEdit.jsp";
//        String source = FileUtils.readFileToString(new File(sourceFile), "UTF-8");
        String source = "<td><select name=\"status\" editable=\"false\" style=\"height:27px;\" panelHeight=\"auto\" class=\"easyui-combobox\"\n" +
                "                                data-options=\"required:true\">\n" +
                "                        <c:forEach items=\"${allStatuss}\" var=\"e\"><option value=\"${e.key}\">${e.value}</option></c:forEach>\n" +
                "                    </select></td>" +
                "        <td><c:forEach items=\"${allKeys}\" var=\"e\"><option value=\"${e.key}\">${e.value}</option></c:forEach></td>";
//        System.out.println(source);

        String open = "<c:forEach";
        String close = "</c:forEach>";
        JspToFreemarkers.forEachConvert(source, "c:forEach", (e) -> {
            return JspToFreemarkers.forEachToList(e);
        });

    }

    /**
     * * <li>jsp：<c:forEach items="${allStatuss}" var="e"><option value="${e.key}">${e.value}</option></c:forEach></li>
     * * <li>ftl: <#list allStatuss as k,v><option value="${k}">${v}</option></#list></li>
     */
    @Test
    public void testFinder() {
//        String jsp = "<c:forEach items=\"${allStatuss}\" var=\"e\"><option value=\"${e.key}\">${e.value}</option></c:forEach>";
        String jsp = "<c:forEach items=\"${customer}\" var=\"c\"><option value=\"${c.name}\">${e.key}</option></c:forEach>";
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
        System.out.println("ftl: " + ftl);
    }


    @Test
    public void testReplace() throws Exception {
        String sourceFile = "/Users/zhangpu/workspace/acooly/v5/acooly-showcase/acooly-showcase-core/" +
                "src/main/resources/META-INF/resources/WEB-INF/jsp/manage/showcase/customerEdit.jsp";
        String source = FileUtils.readFileToString(new File(sourceFile), "UTF-8");
        source = Strings.replaceAll(source, "\\$\\{pageContext.request.contextPath\\}", "");
        source = Strings.replaceAll(source, "<%@ taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\" %>", "");
        source = Strings.replaceAll(source, "<%@ page contentType=\"text/html;charset=UTF-8\" %>", "");
        source = Strings.replaceAll(source, "<%@ include file=\"/WEB-INF/jsp/manage/common/taglibs.jsp\" %>", "<#assign jodd=JspTaglibs[\"http://www.springside.org.cn/jodd_form\"] />");
        source = Strings.replaceAll(source, "jodd:form", "@jodd.form");

        // 问号表达式转换
        source = ftlQExp(source);


//        Document document = Jsoup.parse(source);
//
        HtmlTable htmlTable = tableConvert(source);
//
//        Elements forms = document.getElementsByTag("form");
//        for (Element form : forms) {
//            form.attr("class", "form-horizontal");
//        }
//        source = document.body().html();
//
//        // 返转移被Jsoup转移的字符
//        source = StringEscapeUtils.unescapeHtml(source);
        System.out.println(source);

        // ftl: <#if action=='create'>saveJson<#else>updateJson</#if>
        // jsp: ${action=='create'?'saveJson':'updateJson'}
        // jsp正则：\${\\w*==\\w*\?\\w*:\\w*}
//        String jsp = "121212${action=='create'?'saveJson':'updateJson'}1212121,<b>ddd</b>${action==1?2:20}";
//        System.out.println(qExp(jsp));

    }

    public HtmlTable tableConvert(String source) {
        Iterator<Jerry> trs = Jerry.of(source).s("table").filter(".tableForm").children().iterator();
        HtmlTable htmlTable = new HtmlTable();
        while (trs.hasNext()) {
            HtmlRow htmlRow = new HtmlRow();
            Iterator<Jerry> tds = trs.next().children().iterator();
            List<Integer> viewports = Lists.newArrayList();
            int total = 0;
            int maxCol = 0;
            while (tds.hasNext()) {
                Jerry td = tds.next();
                String tagName = td.get(0).getNodeName();
                if (!Strings.equalsIgnoreCase("td", tagName) && !Strings.equalsIgnoreCase("th", tagName)) {
                    continue;
                }
                HtmlCol htmlCol = new HtmlCol();
                htmlCol.type = HtmlColType.to(tagName);
                htmlCol.content = convertFormData(td);
                String colspanValue = td.attr("colspan");
                int colspan = 1;
                if (Strings.isNumber(colspanValue)) {
                    colspan = Integer.valueOf(colspanValue);
                }
                htmlCol.colspan = colspan;
                viewports.add(colspan);
                maxCol = maxCol + colspan;
                htmlRow.add(htmlCol);
            }

            if (maxCol == 2) {
                // 2 -- 转换为 --> 3:9
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    htmlCol.viewport = (htmlCol.type == HtmlColType.label ? 3 : 9);
                }
            } else if (maxCol == 4) {
                // 4 -> 2:4:2:4, 2:10
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    int viewport = viewports.get(i);
                    if (htmlCol.type == HtmlColType.label) {
                        htmlCol.viewport = 2;
                    } else {
                        htmlCol.viewport = htmlCol.colspan == 1 ? 4 : 10;
                    }
                }
            } else {
                // 其他情况：等分
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    int viewport = viewports.get(i);
                    int split = Double.valueOf(viewport * 12 / (1.0 * total)).intValue();
                    htmlCol.viewport = split;
                }
            }
            htmlTable.add(htmlRow);
        }
        return htmlTable;
    }


    public HtmlTable tableConvert(Document document) {
        Elements tables = document.getElementsByAttributeValue("class", "tableForm");
        Element table = Collections3.getFirst(tables);
        HtmlTable htmlTable = new HtmlTable();
        Elements trs = table.getElementsByTag("tr");
        for (Element tr : trs) {
            HtmlRow htmlRow = new HtmlRow();
            Elements tds = tr.getAllElements();
            List<Integer> viewports = Lists.newArrayList();
            int total = 0;
            int maxCol = 0;
            for (Element td : tds) {
                String tagName = td.nodeName();
                if (!Strings.equalsIgnoreCase("td", tagName) && !Strings.equalsIgnoreCase("th", tagName)) {
                    continue;
                }
                HtmlCol htmlCol = new HtmlCol();
                htmlCol.type = HtmlColType.to(tagName);
                htmlCol.content = convertFormData(td);
                String colspanValue = td.attr("colspan");
                int colspan = 1;
                if (Strings.isNumber(colspanValue)) {
                    colspan = Integer.valueOf(colspanValue);
                }
                htmlCol.colspan = colspan;
                viewports.add(colspan);
                maxCol = maxCol + colspan;
                htmlRow.add(htmlCol);
            }


            if (maxCol == 2) {
                // 2 -- 转换为 --> 3:9
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    htmlCol.viewport = (htmlCol.type == HtmlColType.label ? 3 : 9);
                }
            } else if (maxCol == 4) {
                // 4 -> 2:4:2:4, 2:10
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    int viewport = viewports.get(i);
                    if (htmlCol.type == HtmlColType.label) {
                        htmlCol.viewport = 2;
                    } else {
                        htmlCol.viewport = htmlCol.colspan == 1 ? 4 : 10;
                    }
                }
            } else {
                // 其他情况：等分
                for (int i = 0; i < htmlRow.htmlCols.size(); i++) {
                    HtmlCol htmlCol = htmlRow.htmlCols.get(i);
                    int viewport = viewports.get(i);
                    int split = Double.valueOf(viewport * 12 / (1.0 * total)).intValue();
                    htmlCol.viewport = split;
                }
            }
            htmlTable.add(htmlRow);
        }
        return htmlTable;
    }


    /**
     * JSP问号表达式转FTL
     *
     * @param jsp
     * @return
     */
    public String ftlQExp(String jsp) {
        String regex = "\\$\\{([',\",\\w]*)==([',\",\\w]*)\\?([',\",\\w]*):([',\",\\w]*)\\}";
        String format = "<#if %s==%s>%s<#else>%s</#if>";
        return regexConvert(jsp, regex, format);
    }


    /**
     * JSP转FTL：if表达式
     *
     * <li>jsp：<c:forEach items="${allStatuss}" var="e"><option value="${e.key}">${e.value}</option></c:forEach></li>
     * <li>ftl: <#list allStatuss as k,v><option value="${k}">${v}</option></#list></li>
     *
     * @param jsp
     * @return
     */
    public String ftlIfExp(String jsp) {
        String regex = "\\$\\{([',\",\\w]*)==([',\",\\w]*)\\?([',\",\\w]*):([',\",\\w]*)\\}";
        Jerry jsps = Jerry.of(jsp);
        log.info("items:{}", jsps.attr("items"));
        log.info("var:{}", jsps.attr("var"));
        return null;
    }

    public String regexConvert(String text, String regex, String format) {
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

    public String convertFormData(Jerry td) {
        Iterator<Jerry> elements = td.children().iterator();
        while (elements.hasNext()) {
            Jerry e = elements.next();
            String tagName = e.get(0).getNodeName();
            if (Strings.equalsIgnoreCase(tagName, "input") ||
                    Strings.equalsIgnoreCase(tagName, "textarea")) {
                e.removeClass("text");
                e.addClass("form-control");
            }
            if (Strings.equalsIgnoreCase(tagName, "select")) {
                e.removeAttr("editable");
                e.removeAttr("panelHeight");
                e.removeAttr("style");
                e.removeClass("easyui-combobox");
                e.addClass("form-control");
                e.addClass("select2bs4");
            }
        }
        return Strings.trimToEmpty(td.html());
    }

    public String convertFormData(Element td) {
        Elements elements = td.getAllElements();
        for (Element e : elements) {
            // 输入框表单 input/textarea - text -> form-control
            if (Strings.equalsIgnoreCase(e.nodeName(), "input") ||
                    Strings.equalsIgnoreCase(e.nodeName(), "textarea")) {
                e.removeClass("text");
                e.addClass("form-control");
            }

            if (Strings.equalsIgnoreCase(e.nodeName(), "select")) {
                e.removeAttr("editable");
                e.removeAttr("panelHeight");
                e.removeAttr("style");
                e.removeClass("easyui-combobox");
                e.addClass("form-control");
                e.addClass("select2bs4");
                // 处理迭代
                System.out.println(e.html());
            }

            // 下拉框
            // <select name="gender" id="manage_customer_editform_gender" editable="false" panelHeight="auto"
            //                                class="easyui-combobox" data-options="required:true">
            // <c:forEach items="${allGenders}" var="e">
            //   <option value="${e.key}">${e.value}</option>
            // </c:forEach>
            // </select>

            // 					<select name="idcardType" class="form-control select2bs4" data-options="required:true">
            //						<#list allIdcardTypes as k,v><option value="${k}">${v}</option></#list>
            //					</select>


        }

        return td.html();
    }


    public static class HtmlTable {
        public List<HtmlRow> HtmlRows = Lists.newArrayList();

        public void add(HtmlRow htmlRow) {
            this.HtmlRows.add(htmlRow);
        }
    }

    public static class HtmlRow {
        public List<HtmlCol> htmlCols = Lists.newArrayList();

        public void add(HtmlCol htmlCol) {
            this.htmlCols.add(htmlCol);
        }
    }

    public static class HtmlCol {

        /**
         * 份额:总共12份
         */
        public int viewport;

        public int colspan;

        public HtmlColType type;

        /**
         * 内容
         */
        public String content;

        public HtmlCol() {
        }

        public HtmlCol(int viewport, String content) {
            this.viewport = viewport;
            this.content = content;
        }
    }

    public static enum HtmlColType {
        label, data;

        public static HtmlColType to(String tagName) {
            if (Strings.equalsIgnoreCase(tagName, "td")) {
                return HtmlColType.data;
            } else if (Strings.equalsIgnoreCase(tagName, "th")) {
                return HtmlColType.label;
            }
            return null;
        }
    }


}
