package com.acooly.core.common.web.taglib;

import com.acooly.core.common.dao.support.PageInfo;
import org.apache.commons.beanutils.PropertyUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 已迁移到acooly-taglibs
 *
 * @author zhangpu
 */
@Deprecated
public class PortalSimplePaging extends TagSupport {
    /**
     * 序列号版本
     */
    private static final long serialVersionUID = 19801223;

    /**
     * 表单提交
     */
    private static final String PAGE_SCRIPT =
            "<script language=\"JavaScript\">"
                    + "function goToByLabel(f, label){\n"
                    + " var currentPage = parseInt(document.getElementById('currentPage').value);"
                    + " if(label==\"last\"){"
                    + "  currentPage=parseInt(document.getElementById('totalPage').value);"
                    + " }"
                    + " else if(label==\"first\"){"
                    + "  currentPage=1;"
                    + " }"
                    + " else if(label==\"previous\"){"
                    + "  currentPage--;"
                    + " }"
                    + " else if(label==\"next\"){"
                    + "  currentPage++;"
                    + " }"
                    + " else {"
                    + "  currentPage=parseInt(label);"
                    + " }"
                    + " document.getElementById('currentPage').value = currentPage;"
                    + "	if(&checkMethod&){"
                    + "	f.submit();\n"
                    + "	}"
                    + "}"
                    + ""
                    + ""
                    + ""
                    + ""
                    + ""
                    + "</script>";

    /** */
    private static final Map<Object, Object> PAGING_OPTIONS;

    static {
        PAGING_OPTIONS = new LinkedHashMap<Object, Object>();
        PAGING_OPTIONS.put("10", "10");
        PAGING_OPTIONS.put("20", "20");
        PAGING_OPTIONS.put("30", "30");
        PAGING_OPTIONS.put("40", "40");
        PAGING_OPTIONS.put("50", "50");
        PAGING_OPTIONS.put("60", "60");
        PAGING_OPTIONS.put("70", "70");
        PAGING_OPTIONS.put("80", "80");
        PAGING_OPTIONS.put("90", "90");
        PAGING_OPTIONS.put("100", "100");
    }

    /*
     * example propertyRoot.property2.properties properties must is List;
     */
    private String name;

    /**
     * 上下文中分页对象名称
     */
    private String property = "pageInfo";

    private String scope;

    private String byPage;

    /*
     * 容器样式属性 style example containerStyle="height:12;name:xxxx;"
     */
    private String containerParam;

    /**
     * 向后显示多少页码
     */
    private String showPageNo;

    /*
     *
     */
    private String panelRowParam;

    /*
     *
     */
    private String buttonParam;

    private String checkMethod;

    private PageInfo<Object> getPageInfo() throws JspException {
        Object obj = this.pageContext.getRequest().getAttribute(this.property);
        if (obj != null) {
            if (PropertyUtils.isReadable(obj, "countOfCurrentPage")
                    && PropertyUtils.isReadable(obj, "currentPage")
                    && PropertyUtils.isReadable(obj, "totalPage")
                    && PropertyUtils.isReadable(obj, "totalCount")
                    && PropertyUtils.isReadable(obj, "pageResults")) {
                PageInfo<Object> pageInfo = new PageInfo<Object>();
                try {
                    PropertyUtils.copyProperties(pageInfo, obj);
                    return pageInfo;
                } catch (Exception e) {
                    throw new JspException(e);
                }
            } else {
                throw new JspException("分页对象属性不完整,无法完成初始化.");
            }
        } else {
            throw new JspException("分页对象为空,无法完成初始化.");
        }
    }

    private Map<Object, Object> convertProperties(
            String prop, String separator, String subSeparator) {
        if (prop == null || prop.equals("")) {
            return null;
        } else {
            String[] props = prop.trim().split(separator);
            Map<Object, Object> m = new LinkedHashMap<Object, Object>(props.length);
            for (int i = 0; i < props.length; i++) {
                String[] keyValue = props[i].split(subSeparator);
                if (keyValue != null && keyValue.length > 0) {
                    if (keyValue.length > 1) {
                        m.put(keyValue[0], keyValue[1]);
                    } else {
                        m.put(keyValue[0], null);
                    }
                }
            }
            return m;
        }
    }

    private Map<Object, Object> convertProperties(String prop) {
        return this.convertProperties(prop, ";", ":");
    }

    private String makeStartTag(String tag, String style) {
        StringBuilder buf = new StringBuilder();
        buf.append("<" + tag + " class=\"pageNav\"");
        if (style != null && !style.equals("")) {
            Map<Object, Object> m = this.convertProperties(style);
            for (Map.Entry<Object, Object> entry : m.entrySet()) {
                buf.append(" ");
                Object k = entry.getKey();
                Object v = entry.getValue();
                buf.append(k + "=\"" + v + "\"");
            }
        }
        buf.append(">");
        return buf.toString();
    }

    private String makeEndTag(String tag) {
        StringBuilder buf = new StringBuilder();
        buf.append("</" + tag + ">");
        return buf.toString();
    }

    @SuppressWarnings("unused")
    private String makeButton(String label, String value, boolean disable) {
        String style =
                "type:button;"
                        + "value=\"\""
                        + " class=\""
                        + label
                        + "\""
                        + " onclick:goToByLabel(document."
                        + this.name
                        + ",'"
                        + value
                        + "');"
                        + (disable ? "disabled:disabled" : "");
        String button = this.makeStartTag("input", this.buttonParam + ";" + style);
        return button;
    }

    private String makeHref(String pageNo) {
        String href =
                "<a href=\"javascript:goToByLabel(document.getElementById('"
                        + this.name
                        + "'),'"
                        + pageNo
                        + "')\"";
        if (pageNo.equals("next")) {
            href += " >" + "下一页" + "</a>";
        } else if (pageNo.equals("previous")) {
            href += " >" + "上一页" + "</a>";
        } else if (pageNo.equals("first")) {
            href += " >" + "首页" + "</a>";
        } else if (pageNo.equals("last")) {
            href += " >" + "末页" + "</a>";
        } else {
            href += " >" + pageNo + "</a>";
        }
        return href;
    }

    private String makeHidden(String name, String id, String value) {
        return "<input type=\"hidden\" name=\""
                + name
                + "\" id=\""
                + id
                + "\" value=\""
                + value
                + "\">";
    }

    @SuppressWarnings("unused")
    private String makeSelector(String style, Map<Object, Object> options, Object selected) {
        StringBuffer buf = new StringBuffer();
        buf.append(this.makeStartTag("select", style));
        for (Map.Entry<Object, Object> entry : options.entrySet()) {
            Object k = entry.getKey();
            Object v = entry.getValue();
            buf.append(
                    "<option value=\""
                            + v
                            + "\""
                            + (v.equals(selected) ? "selected" : "")
                            + ">"
                            + k
                            + "</option>");
        }
        buf.append(this.makeEndTag("select"));
        return buf.toString();
    }

    public String getButtonParam() {
        return buttonParam;
    }

    public void setButtonParam(String buttonParam) {
        this.buttonParam = buttonParam;
    }

    public String getContainerParam() {
        return containerParam;
    }

    public void setContainerParam(String containerParam) {
        this.containerParam = containerParam;
    }

    public String getPanelRowParam() {
        return panelRowParam;
    }

    public void setPanelRowParam(String panelRowParam) {
        this.panelRowParam = panelRowParam;
    }

    @Override
    public int doEndTag() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();

        PageInfo<Object> pageInfo = this.getPageInfo();
        int currentPage = pageInfo.getCurrentPage();
        long totalPage = pageInfo.getTotalPage();
        long totalCount = pageInfo.getTotalCount();

        int first = 1;
        int previous = currentPage - 1;
        int next = currentPage + 1;
        long last = totalPage;

        try {
            if (this.checkMethod == null || this.checkMethod.equals("")) {
                out.println(PAGE_SCRIPT.replaceFirst("&checkMethod&", "true"));
            } else {
                out.println(PAGE_SCRIPT.replaceFirst("&checkMethod&", this.checkMethod));
            }
            // <table>
            out.println(this.makeStartTag("table", this.containerParam));
            // output paging control panel

            out.println(this.makeStartTag("tr", this.panelRowParam));
            out.println(this.makeStartTag("td", "align:left"));
            // left adding here
            if (currentPage > 1) {
                out.println(this.makeHref("first"));
            }
            if (currentPage > 1) {
                out.println(this.makeHref("previous"));
            }
            /** 当前最大可能页数 */
            if (totalPage > 1) {
                int maxCurrentPage = currentPage + Integer.parseInt(this.showPageNo);
                if (currentPage % Integer.parseInt(this.showPageNo) == 0) {
                    if (maxCurrentPage <= totalPage) {
                        for (int i = currentPage; i < maxCurrentPage; i++) {
                            if (i == currentPage) {
                                out.println("<span>" + i + "</span>");
                            } else {
                                out.println(makeHref(String.valueOf(i)));
                            }
                        }
                    } else {
                        int total = currentPage + Integer.parseInt(this.showPageNo);
                        if (total > totalPage) {
                            total = (int) totalPage;
                        }
                        int count = 0;
                        for (int i = currentPage; i <= total; i++) {
                            count++;
                            if (count > Integer.parseInt(this.showPageNo)) {
                                break;
                            }
                            if (i == currentPage) {
                                out.println("<span>" + i + "</span>");
                            } else {
                                out.println(makeHref(String.valueOf(i)));
                            }
                        }
                    }
                } else {
                    int mod = currentPage % Integer.parseInt(this.showPageNo);
                    int thisCurrentPage = currentPage - mod;
                    if (thisCurrentPage == 0) {
                        thisCurrentPage += 1;
                    }
                    if (maxCurrentPage <= totalPage) {
                        int temp = currentPage - mod + Integer.parseInt(this.showPageNo);
                        if (currentPage - mod == 0) {
                            temp += 1;
                        }
                        for (int i = thisCurrentPage; i < temp; i++) {
                            if (i == currentPage) {
                                out.println("<span>" + i + "</span>");
                            } else {
                                out.println(makeHref(String.valueOf(i)));
                            }
                        }
                    } else {
                        int total = currentPage - mod + Integer.parseInt(this.showPageNo);
                        if (total > totalPage) {
                            total = Integer.valueOf(String.valueOf(totalPage));
                        }
                        int count = 0;
                        for (int i = thisCurrentPage; i <= total; i++) {
                            count++;
                            if (count > Integer.parseInt(this.showPageNo)) {
                                break;
                            }
                            if (i == currentPage) {
                                out.println("<span>" + i + "</span>");
                            } else {
                                out.println(makeHref(String.valueOf(i)));
                            }
                        }
                    }
                }
            }
            if (currentPage < totalPage) {
                out.println(this.makeHref("next"));
            }
            if (currentPage < totalPage) {
                out.println(this.makeHref("last"));
            }

            // adding hidden element here
            out.println(this.makeHidden("first", "first", String.valueOf(first)));
            out.println(this.makeHidden("previous", "previous", String.valueOf(previous)));
            out.println(this.makeHidden("next", "next", String.valueOf(next)));
            out.println(this.makeHidden("last", "last", String.valueOf(last)));
            out.println(
                    this.makeHidden("currentPage", "currentPage", String.valueOf(pageInfo.getCurrentPage())));
            out.println(
                    this.makeHidden("totalPage", "totalPage", String.valueOf(pageInfo.getTotalPage())));
            out.println(this.makeEndTag("td"));
            // -------------------------------------
            out.println(this.makeStartTag("td", "align:right"));
            // right adding here
            out.println("<span class='pageNavS'>当前页码:</span>" + currentPage);
            out.println("<span class='pageNavS'>总页数:</span>" + totalPage);
            out.println("<span class='pageNavS'>总记录数:</span>" + totalCount);
            out.println(this.makeEndTag("td"));
            out.println(this.makeEndTag("tr"));
            // </table>
            out.println(this.makeEndTag("table"));
        } catch (IOException ie) {
            throw new JspException(ie.getMessage());
        }

        return SKIP_BODY;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public String getByPage() {
        return byPage;
    }

    public void setByPage(String byPage) {
        this.byPage = byPage;
    }

    public String getShowPageNo() {
        return showPageNo;
    }

    public void setShowPageNo(String showPageNo) {
        this.showPageNo = showPageNo;
    }
}
