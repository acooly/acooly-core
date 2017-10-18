package com.acooly.module.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** @author qiubo@yiji.com */
public class HttpsOnlyFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (!request.isSecure()) {
      ((HttpServletResponse) response).sendRedirect(buildHttpsUrl((HttpServletRequest) request));
    } else {
      ((HttpServletResponse) response)
          .setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
      chain.doFilter(request, response);
    }
  }

  private String buildHttpsUrl(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder("https://");
    sb.append(request.getServerName());
    sb.append(request.getRequestURI());
    if (request.getQueryString() != null) {
      sb.append('?');
      sb.append(request.getQueryString());
    }
    return sb.toString();
  }

  @Override
  public void destroy() {}
}
