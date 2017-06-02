package com.acooly.module.security.defence;

import com.acooly.module.security.config.SecurityProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Cross Site Scripting 跨站脚本攻击防御
 *
 * @author zhangpu
 */
public class XssDefenseFilter implements Filter {

  private SecurityProperties securityProperties;

  public SecurityProperties getSecurityProperties() {
    return securityProperties;
  }

  public void setSecurityProperties(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  public void init(FilterConfig filterConfig) throws ServletException {}

  public void destroy() {}

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (securityProperties.getXss().matches((HttpServletRequest) request)) {
      chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
    } else {
      chain.doFilter(request, response);
    }
  }
}
