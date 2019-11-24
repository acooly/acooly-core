package com.acooly.module.defence.xss;

import com.acooly.module.defence.DefenceProperties;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Cross Site Scripting 跨站脚本攻击防御
 *
 * @author zhangpu
 */
public class XssDefenseFilter implements Filter {

    @Getter
    @Setter
    private DefenceProperties defenceProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (defenceProperties.getXss().matches((HttpServletRequest) request)) {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
