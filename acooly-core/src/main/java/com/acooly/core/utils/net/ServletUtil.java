package com.acooly.core.utils.net;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shuijing
 */
public class ServletUtil {

    public static Object getRequestAttribute(String name) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        return sra.getAttribute(name, ServletWebRequest.SCOPE_REQUEST);
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        return sra.getRequest();
    }

    public static String getRequestParameter(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes()).getRequest();
        return request.getParameter(name);
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();
    }

    public static void setRequestAttribute(String name, Object value) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        sra.setAttribute(name, value, ServletWebRequest.SCOPE_REQUEST);
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getResponse();
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
            .getSession(true);
    }

    public static Object getSessionAttribute(String sessionKey) {
        return RequestContextHolder.currentRequestAttributes().getAttribute(sessionKey,
            RequestAttributes.SCOPE_SESSION);
    }

    public static void setSessionAttribute(String sessionKey, Object value) {
        RequestContextHolder.currentRequestAttributes().setAttribute(sessionKey, value,
            RequestAttributes.SCOPE_SESSION);
    }
}
