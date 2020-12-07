package com.acooly.core.utils;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.conversion.StringToDateConverter;
import com.acooly.core.utils.conversion.StringYuanToMoneyConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Nullable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * Servlet工具类.
 *
 * @author zhangpu
 */
public class Servlets {

    // -- 常用数值定义 --//
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
    private static final DefaultConversionService CONVERSION_SERVICE = new DefaultConversionService();

    static {
        CONVERSION_SERVICE.addConverter(new StringYuanToMoneyConverter());
        CONVERSION_SERVICE.addConverter(new StringToDateConverter());
    }

    /**
     * 根据URL获取根服务路径
     *
     * @param urlString
     * @return
     */
    public static String getServerRoot(String urlString) {
        try {
            URL url = new URL(urlString);
            StringBuilder sb = new StringBuilder();
            sb.append(url.getProtocol()).append("://").append(url.getAuthority());
            return sb.toString();
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCodes.PARAMETER_ERROR, "URL格式错误或解析错误");
        }
    }


    /**
     * 重定向(redirect)
     *
     * @param response
     * @param location
     */
    public static void redirect(HttpServletResponse response, String location) {
        try {
            response.sendRedirect(location);
        } catch (Exception e) {
            throw new RuntimeException("重定向失败,location:" + location + " :", e);
        }
    }

    /**
     * 转发(forward)
     *
     * @param request
     * @param response
     * @param path
     */
    public static void forward(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException("转发失败,path:" + path + " :", e);
        }
    }


    /**
     * 写入字符串到HttpServletResponse
     *
     * @param response
     * @param data
     * @param contentType 可选 默认JSON_UTF-8
     */
    public static void writeResponse(@NotNull HttpServletResponse response, @NotNull String data, @Nullable String contentType) {
        response.setCharacterEncoding("UTF-8");
        if (Strings.isBlank(contentType)) {
            contentType = MediaType.JSON_UTF_8.toString();
        }
        response.setContentType(contentType);
        try (InputStream input = new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8")));
             OutputStream output = response.getOutputStream();) {
            IOUtils.copy(input, output);
            output.flush();
        } catch (Exception e) {
            throw new BusinessException("响应请求(flushResponse)失败:" + e.getMessage());
        }
    }

    /**
     * 写入字符串到HttpServletResponse(JSON_UTF-8)
     * contentType：JSON_UTF_8
     *
     * @param response
     * @param data
     */
    public static void writeResponse(HttpServletResponse response, String data) {
        writeResponse(response, data, MediaType.JSON_UTF_8.toString());
    }

    /**
     * 写入字符串到HttpServletResponse
     * contentType：PLAIN_TEXT_UTF_8
     *
     * @param response
     * @param data
     */
    public static void writeText(HttpServletResponse response, String data) {
        writeResponse(response, data, MediaType.PLAIN_TEXT_UTF_8.toString());
    }

    /**
     * 从request中获取body
     *
     * @param request
     * @return
     */
    public static String getBody(HttpServletRequest request, String encoding) {
        try (InputStream in = request.getInputStream()) {
            encoding = Strings.isBlankDefault(encoding, "UTF-8");
            return StreamUtils.copyToString(in, Charset.forName(encoding));
        } catch (Exception e) {
            throw Exceptions.runtimeException("读取HttpRequest的body失败", e);
        }
    }

    /**
     * 从request中获取body
     * 编码：UTF-8
     *
     * @param request
     * @return
     */
    public static String getBody(HttpServletRequest request) {
        return getBody(request, null);
    }


    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set a fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set a time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     *
     * <p>如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(
            HttpServletRequest request, HttpServletResponse response, long lastModified) {
        long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     *
     * <p>如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(
            HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * <p>返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        return Maps.newHashMap(Maps.transformValues(getParameters(request, prefix, false), v -> v));
    }

    public static Map<String, String> getParameters(ServletRequest request, String prefix, boolean includeEmpty) {
        Validate.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> params = Maps.newLinkedHashMap();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String key = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);
                if (!includeEmpty && StringUtils.isBlank(value)) {
                    continue;
                }
                params.put(key, value);
            }
        }
        return params;
    }

    public static Map<String, String> getParameters(ServletRequest request) {
        return getParameters(request, null, true);
    }


    public static <T> T getParameter(ServletRequest request, String name, Class<T> clazz) {
        String value = request.getParameter(name);
        if (Strings.isBlank(value)) {
            return null;
        }

        return CONVERSION_SERVICE.convert(value, clazz);
    }

    public static <T> T getParameter(String name, Class<T> clazz) {
        String value = Servlets.getRequest().getParameter(name);
        if (Strings.isBlank(value)) {
            return null;
        }
        return CONVERSION_SERVICE.convert(value, clazz);
    }

    public static String getParameter(String name) {
        return Servlets.getRequest().getParameter(name);
    }

    public static String getParameter(ServletRequest request, String name) {
        return request.getParameter(name);
    }

    public static Integer getIntParameter(String name) {
        return getParameter(name, Integer.class);
    }

    public static Integer getIntParameter(ServletRequest request, String name) {
        return getParameter(request, name, Integer.class);
    }

    public static Long getLongParameter(String name) {
        return getParameter(name, Long.class);
    }

    public static Long getLongParameter(ServletRequest request, String name) {
        return getParameter(request, name, Long.class);
    }

    public static Money getMoneyParameter(ServletRequest request, String name) {
        return getParameter(request, name, Money.class);
    }

    public static Money getMoneyParameter(String name) {
        return getParameter(name, Money.class);
    }

    public static Date getDateParameter(ServletRequest request, String name) {
        return getParameter(request, name, Date.class);
    }

    public static Date getDateParameter(String name) {
        return getParameter(name, Date.class);
    }

    /**
     * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
     *
     * @see #getParametersStartingWith
     */
    public static String encodeParameterStringWithPrefix(Map<String, ?> params, String prefix) {
        Asserts.notEmpty(params);
        prefix = Strings.trimToEmpty(prefix);
        StringBuilder queryStringBuilder = new StringBuilder();
        Iterator<? extends Entry<String, ?>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ?> entry = it.next();
            queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append('&');
            }
        }
        return queryStringBuilder.toString();
    }


    public static String buildQueryString(Map<String, String> params) {
        return encodeParameterStringWithPrefix(params, null);
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    /**
     * 获取请求的URL路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        return StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
    }

    public static String getRequestPage(HttpServletRequest request) {
        return StringUtils.substringAfterLast(request.getRequestURI(), "/");
    }

    public static String getRequestFullPath(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (Strings.isBlank(queryString)) {
            return getRequestPath(request);
        } else {
            return getRequestPath(request) + "?" + request.getQueryString();
        }
    }

    public static String getHeaderValue(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isBlank(value)) {
            value = request.getHeader(StringUtils.lowerCase(name));
        }
        return value;
    }

    public static List<String> getHeaderValues(HttpServletRequest request, String name) {
        Enumeration<String> e = request.getHeaders(name);
        List<String> values = Lists.newArrayList();
        while (e.hasMoreElements()) {
            values.add(e.nextElement());
        }
        return values;
    }


    /**
     * 获取Headers
     *
     * @param request
     * @param prefixName        前缀
     * @param includePrefixName key是否包含前缀
     * @return
     */
    public static Map<String, String> getHeaders(HttpServletRequest request, String prefixName, boolean includePrefixName) {
        Enumeration<String> names = request.getHeaderNames();
        String name = null;
        Map<String, String> map = Maps.newLinkedHashMap();
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (Strings.isBlank(prefixName)) {
                map.put(name, getHeaderValue(request, name));
                continue;
            }

            if (StringUtils.startsWithIgnoreCase(name, prefixName)) {
                map.put(includePrefixName ? name : Strings.substringAfter(name, prefixName), getHeaderValue(request, name));
            }
        }
        return map;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request, String prefixName) {
        return getHeaders(request, prefixName, true);
    }


    public static void setHeader(HttpServletResponse response, String name, String value) {
        response.setHeader(name, value);
    }

    public static void setHeaders(HttpServletResponse response, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            setHeader(response, entry.getKey(), entry.getValue());
        }
    }

    public static String getQueryString(HttpServletRequest request) {
        return getRequestPath(request) + "?" + request.getQueryString();
    }

    /**
     * 获取当前请求的Host
     *
     * @param request
     * @return
     */
    public static String getHost(HttpServletRequest request) {
        String host = null;
        try {
            URL url = new URL(request.getRequestURL().toString());
            host = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != 80 && url.getPort() != -1) {
                host += ":" + url.getPort();
            }
        } catch (Exception e) {
        }
        return host;
    }

    /**
     * 获取UserAgent，
     * <p>
     * 使用请增加mave依赖neu.bitwalker:UserAgentUtils
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    public static UserAgent getUserAgent() {
        return UserAgent.parseUserAgentString(getRequest().getHeader("User-Agent"));
    }

    public static Object getRequestAttribute(String name) {
        ServletRequestAttributes sra =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra.getAttribute(name, ServletWebRequest.SCOPE_REQUEST);
    }


    public static String getRequestParameter(String name) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(name);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static void setRequestAttribute(String name, Object value) {
        ServletRequestAttributes sra =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        sra.setAttribute(name, value, ServletWebRequest.SCOPE_REQUEST);
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getSession(true);
    }

    public static Object getSessionAttribute(String sessionKey) {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(sessionKey, RequestAttributes.SCOPE_SESSION);
    }

    public static void setSessionAttribute(String sessionKey, Object value) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute(sessionKey, value, RequestAttributes.SCOPE_SESSION);
    }

}
