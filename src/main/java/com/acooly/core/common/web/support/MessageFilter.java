package com.acooly.core.common.web.support;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 清除session中控制层设置的页面显示消息
 * <p>
 * <li>controler中通过saveMessage把需要显示到页面的消息保持到session变量中
 * <li>本filter中，读取message传存到request中，同时删除session中的message数据
 * <li>页面通过读取request中的message显示用户信息
 * </p>
 * <br>
 * 主要用于解决Redirect中无法直接通过request专递数据到页面的问题。
 * @author zhangpu
 * 迁移到：com.acooly.integration.web
 */
@Deprecated
public class MessageFilter implements Filter {
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
																					throws IOException,
																					ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		List<Object> messages = (List<Object>) request.getSession().getAttribute("messages");
		
		if (messages != null) {
			request.setAttribute("messages", messages);
			request.getSession().removeAttribute("messages");
		}
		chain.doFilter(req, res);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
}
