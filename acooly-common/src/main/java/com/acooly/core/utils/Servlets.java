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
 * <p>
 * 提供Servlet相关的常用工具集，主要包括：
 *
 * <li>参数：请求和响应参数获取和设置，以及自动安全转型</li>
 * <li>会话：会话参数的获取和设置</li>
 * <li>头参：header的获取和设置，包括一些常用的功能性header设置和获取，比如：文件，缓存，安全等</li>
 * <li>数据：请求/响应体的数据获取和写入</li>
 * <li>路径：路径相关的工具解析和设置</li>
 * <li>跳转：redirect/forward</li>
 *
 * @author zhangpu
 * @date 2019-09-01 17:20
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
     * <p>
     * 例如：http://localhost:8080/a/b/c.html?name=abc  -(getServerRoot)-> http://localhost:8080
     *
     * @param urlString 请求URL
     * @return 服务器根路径
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
     * @param response HttpServlet响应对象
     * @param location 重定向地址
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
     * @param request  Servlet请求对象
     * @param response Servlet响应对象
     * @param path     转发路径
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
     * @param response    Servlet响应对象
     * @param data        写入数据
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
            throw new BusinessException(CommonErrorCodes.COMMUNICATION_ERROR, "响应请求(flushResponse)失败:" + e.getMessage());
        }
    }

    /**
     * 写入字符串到HttpServletResponse(JSON_UTF-8)
     * contentType：JSON_UTF_8
     *
     * @param response Servlet响应对象
     * @param data     写入数据
     * @see #writeResponse(HttpServletResponse, String, String)
     */
    public static void writeResponse(HttpServletResponse response, String data) {
        writeResponse(response, data, MediaType.JSON_UTF_8.toString());
    }

    /**
     * 写入字符串到HttpServletResponse(PLAIN_TEXT_UTF_8)
     * contentType：PLAIN_TEXT_UTF_8
     *
     * @param response Servlet响应对象
     * @param data     写入数据
     * @see #writeResponse(HttpServletResponse, String, String)
     */
    public static void writeText(HttpServletResponse response, String data) {
        writeResponse(response, data, MediaType.PLAIN_TEXT_UTF_8.toString());
    }

    /**
     * 从request中获取body
     * 默认为UTF8编码
     *
     * @param request Servlet请求对象
     * @return 请求body内容（UTF-8字符串）
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
     * 默认编码：UTF-8
     *
     * @param request Servlet请求对象
     * @return 请求body内容（UTF-8字符串）
     */
    public static String getBody(HttpServletRequest request) {
        return getBody(request, null);
    }


    /**
     * 设置客户端缓存过期时间.
     * <p>
     * 通过在HttpServletResponse中设置Header实现。
     * 参数为：HttpHeaders.EXPIRES(Http 1.0),HttpHeaders.CACHE_CONTROL(Http 1.1)
     *
     * @param response       Servlet响应对象
     * @param expiresSeconds 过期时间(单位：秒)
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set a fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set a time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的
     *
     * @param response Servlet响应对象
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified.
     *
     * @param response         Servlet响应对象
     * @param lastModifiedDate 最后修改时间
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header
     *
     * @param response Servlet响应对象
     * @param etag     Etag
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     *
     * <p>如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param request      Servlet请求对象
     * @param response     Servlet响应对象
     * @param lastModified 内容的最后修改时间.
     * @return true：已修改；false：未修改
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
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
     * @param request  Servlet请求对象
     * @param response Servlet响应对象
     * @param etag     内容的ETag.
     * @return true：有效；false：无效
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
     * 设置下周文件名称，支持中文
     *
     * @param response Servlet响应对象
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * <p>返回的结果的Parameter名已去除前缀.
     *
     * @param request Servlet请求对象
     * @param prefix  前缀
     * @return 参数Map
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        return Maps.newHashMap(Maps.transformValues(getParameters(request, prefix, false), v -> v));
    }

    /**
     * 获取相同前缀的参数值
     * <p>
     * 值全部为String类型，对于多值不支持
     *
     * @param request      Servlet请求对象
     * @param prefix       前缀
     * @param includeEmpty 是否包含空值
     * @return 参数Map，key:去除前缀后的参数名，value:参数值
     */
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

    /**
     * 获取所有参数值
     * <p>
     * 值全部为String类型，对于多值不支持
     *
     * @param request Servlet请求对象
     * @return 参数Map，key:参数名，value:参数值
     */
    public static Map<String, String> getParameters(ServletRequest request) {
        return getParameters(request, null, true);
    }

    /**
     * 获取参数值，并转换为指定类型
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @param clazz   参数类型
     * @param <T>     参数类型泛型
     * @return (T)参数值
     */
    public static <T> T getParameter(ServletRequest request, String name, Class<T> clazz) {
        String value = request.getParameter(name);
        if (Strings.isBlank(value)) {
            return null;
        }
        return CONVERSION_SERVICE.convert(value, clazz);
    }

    /**
     * 获取参数值，并转换为指定类型
     * <p>
     * 从当前线程变量中获取request
     *
     * @param name  参数名
     * @param clazz 参数类型
     * @param <T>   参数类型泛型
     * @return (T)参数值
     */
    public static <T> T getParameter(String name, Class<T> clazz) {
        String value = Servlets.getRequest().getParameter(name);
        if (Strings.isBlank(value)) {
            return null;
        }
        return CONVERSION_SERVICE.convert(value, clazz);
    }

    /**
     * 获取字符串参数值
     * <p>
     * 从当前线程变量中获取request
     *
     * @param name 参数名
     * @return 参数值
     */
    public static String getParameter(String name) {
        return Servlets.getRequest().getParameter(name);
    }

    /**
     * 获取字符串参数值
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @return 参数值
     */
    public static String getParameter(ServletRequest request, String name) {
        return request.getParameter(name);
    }

    /**
     * 获取线程变量中request的整数参数值
     *
     * @param name 参数名
     * @return 参数值(Integer)
     */
    public static Integer getIntParameter(String name) {
        return getParameter(name, Integer.class);
    }

    /**
     * 获取整数参数值
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @return 参数值(Integer)
     */
    public static Integer getIntParameter(ServletRequest request, String name) {
        return getParameter(request, name, Integer.class);
    }

    /**
     * 获取线程变量中request的长整数参数值
     *
     * @param name 参数名
     * @return 参数值(Long)
     */
    public static Long getLongParameter(String name) {
        return getParameter(name, Long.class);
    }

    /**
     * 获取长整数参数值
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @return 参数值(Long)
     */
    public static Long getLongParameter(ServletRequest request, String name) {
        return getParameter(request, name, Long.class);
    }

    /**
     * 获取Money参数值
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @return 参数值(Money)
     */
    public static Money getMoneyParameter(ServletRequest request, String name) {
        return getParameter(request, name, Money.class);
    }

    /**
     * 获取取线程变量中的Money参数值
     *
     * @param name 参数名
     * @return 参数值(Money)
     */
    public static Money getMoneyParameter(String name) {
        return getParameter(name, Money.class);
    }

    /**
     * 获取Date参数值
     *
     * @param request Servlet请求对象
     * @param name    参数名
     * @return 参数值(Date)
     */
    public static Date getDateParameter(ServletRequest request, String name) {
        return getParameter(request, name, Date.class);
    }

    /**
     * 获取线程变量中request的Date参数值
     *
     * @param name 参数名
     * @return 参数值(Date)
     */
    public static Date getDateParameter(String name) {
        return getParameter(name, Date.class);
    }

    /**
     * 生成QueryString,支持参数加前缀
     * <p>
     * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
     * 形如：a=1&b=2&c=3
     *
     * @param params 参数Map，key:参数名，value:参数值
     * @param prefix 参数名前缀
     * @return QueryString
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

    /**
     * 生成QueryString
     *
     * @param params 参数Map，key:参数名，value:参数值
     * @return QueryString
     */
    public static String buildQueryString(Map<String, String> params) {
        return encodeParameterStringWithPrefix(params, null);
    }

    /**
     * 生成Http Basic验证的 Header进行编码.
     *
     * @param userName 用户名
     * @param password 密码
     * @return 编码后的Header
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    /**
     * 获取请求的URL路径
     * 相对路径，不包含ContextPath。形如：/a/b/c.html
     *
     * @param request Servlet请求对象
     * @return 请求的URL路径
     */
    public static String getRequestPath(HttpServletRequest request) {
        return StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
    }

    /**
     * 获取请求的URL的页面名称
     * <p>
     * 形如： c.html
     *
     * @param request Servlet请求对象
     * @return 请求的URL的页面名称
     */
    public static String getRequestPage(HttpServletRequest request) {
        return StringUtils.substringAfterLast(request.getRequestURI(), "/");
    }

    /**
     * 获取请求的URL的完整路径
     * <p>
     * 包括：requestPath + queryString
     *
     * @param request Servlet请求对象
     * @return 请求的URL的完整路径
     */
    public static String getRequestFullPath(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (Strings.isBlank(queryString)) {
            return getRequestPath(request);
        } else {
            return getRequestPath(request) + "?" + request.getQueryString();
        }
    }

    /**
     * 获取请求的制定名称的header值
     *
     * @param request Servlet请求对象
     * @param name    header名称
     * @return header值
     */
    public static String getHeaderValue(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isBlank(value)) {
            value = request.getHeader(StringUtils.lowerCase(name));
        }
        return value;
    }

    /**
     * 获取请求的制定名称的header集合值
     *
     * @param request Servlet请求对象
     * @param name    header名称
     * @return 集合值
     */
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
     * @param request           Servlet请求对象
     * @param prefixName        前缀
     * @param includePrefixName key是否包含前缀
     * @return http头参数，Map<String, String>
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

    /**
     * 获取Headers
     * <p>
     * 值都转换为String，包括制定的前缀名称
     *
     * @param request    Servlet请求对象
     * @param prefixName 前缀
     * @return http头参数，Map<String, String>
     */
    public static Map<String, String> getHeaders(HttpServletRequest request, String prefixName) {
        return getHeaders(request, prefixName, true);
    }

    /**
     * 设置header值
     *
     * @param response Servlet响应对象
     * @param name     header名称
     * @param value    header值
     */
    public static void setHeader(HttpServletResponse response, String name, String value) {
        response.setHeader(name, value);
    }

    /**
     * 设置header值（Map多值）
     *
     * @param response Servlet响应对象
     * @param map      headers的键值对
     */
    public static void setHeaders(HttpServletResponse response, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            setHeader(response, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取请求的QueryString
     * 形如：a=1&b=2
     *
     * @param request Servlet请求对象
     * @return queryString
     */
    public static String getQueryString(HttpServletRequest request) {
        return getRequestPath(request) + "?" + request.getQueryString();
    }

    /**
     * 获取当前请求的Host
     *
     * @param request Servlet请求对象
     * @return host主机名，包含端口号
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
     *
     * @param request Servlet请求对象
     * @return UserAgent
     * @see <a href="https://mvnrepository.com/artifact/eu.bitwalker/UserAgentUtils">UserAgentUtils</a>
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * 获取UserAgent，从线程命令中的Request获取
     *
     * @return UserAgent
     */
    public static UserAgent getUserAgent() {
        return UserAgent.parseUserAgentString(getRequest().getHeader("User-Agent"));
    }

    /**
     * 获取请求属性值，从线程变量中获取
     *
     * @param name 属性名称
     * @return 属性值（Object）
     */
    public static Object getRequestAttribute(String name) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra.getAttribute(name, ServletWebRequest.SCOPE_REQUEST);
    }

    /**
     * 设置请求属性值到当前线程中的Request中
     *
     * @param name  属性名称
     * @param value 属性值
     */
    public static void setRequestAttribute(String name, Object value) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        sra.setAttribute(name, value, ServletWebRequest.SCOPE_REQUEST);
    }

    /**
     * 获取请求参数,从线程变量中
     *
     * @param name 参数名称
     * @return 参数值 （String）
     */
    public static String getRequestParameter(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(name);
    }

    /**
     * 获取当前线程中的HttpServletRequest对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取当前线程中的HttpServletResponse对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取当前线程中的HttpSession对象
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getSession(true);
    }

    /**
     * 获取当前线程中的Session参数
     *
     * @param sessionKey session参数名称
     * @return session参数值
     */
    public static Object getSessionAttribute(String sessionKey) {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(sessionKey, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 设置Session参数值到当前线程中
     *
     * @param sessionKey session参数名称
     * @param value      session参数值
     */
    public static void setSessionAttribute(String sessionKey, Object value) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute(sessionKey, value, RequestAttributes.SCOPE_SESSION);
    }

}
