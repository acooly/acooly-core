/**
 * create by zhangpu
 * date:2015年10月12日
 */
package com.acooly.integration.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 静态资源访问filter
 * 
 * @author zhangpu
 * @date 2015年10月12日
 */
public class StaticResourceSpringFilter extends OncePerRequestFilter {

	@Resource(name = "staticResourceHttpRequestHandler")
	private StaticResourceHttpRequestHandler staticResourceHttpRequestHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		staticResourceHttpRequestHandler.handleRequest(request, response);
	}

}
