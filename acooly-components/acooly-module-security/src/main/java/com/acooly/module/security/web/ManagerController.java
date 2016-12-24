package com.acooly.module.security.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.module.security.config.SecurityConfig;
import com.acooly.module.security.config.SecurityConfigHolder;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;

/**
 * 登录及主页
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author zhangpu
 */
@Controller
@RequestMapping(value = "/manage/")
public class ManagerController extends AbstractJQueryEntityController<User, UserService> {

	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Override
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/manage/index";
	}

	/**
	 * GET访问，直接进入登陆界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, Model model) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			// 如果已经登录的情况，直接回到主框架界面
			return "redirect:/manage/index.html";
		} else {
			// 如果没有登录的首次进入登录界面，直接返回到登录界面。
			SecurityConfig securityConfig = SecurityConfigHolder.getSecurityConfig();
			// model.addAttribute("securityConfig", securityConfig);
			request.getSession(true).setAttribute("securityConfig", securityConfig);
			return "/manage/login";
		}
	}

	@RequestMapping(value = "onLoginSuccess")
	@ResponseBody
	public JsonResult onLoginSuccess(HttpServletRequest request) {
		logger.debug("OnLoginSuccess, redirect to index.jsp");
		return new JsonResult(true);
	}

	@RequestMapping(value = "onLoginFailure")
	@ResponseBody
	public JsonResult onLoginFailure(HttpServletRequest request) {
		logger.debug("OnLoginFailure");
		// 获取Shiro的错误信息
		String obj = request.getParameter(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = request.getParameter("message");
		String msg = "";
		if (obj != null) {
			if ("org.apache.shiro.authc.UnknownAccountException".equals(obj)) {
				msg = "用户名或密码错误";
			} else if ("org.apache.shiro.authc.IncorrectCredentialsException".equals(obj)) {
				msg = "用户名或密码错误";
			} else if ("com.acooly.module.security.shiro.exception.InvaildCaptchaException".equals(obj)) {
				msg = "验证码错误";
			} else if ("org.apache.shiro.authc.AuthenticationException".equals(obj)) {
				msg = message;
			} else {
				msg = message;
			}
			logger.debug(msg + " --> " + obj + ": " + message);
		}
		JsonResult result = new JsonResult(obj, msg);
		result.appendData(Servlets.getParameters(request, null, false));
		return result;
	}

	/**
	 * 会话过期，或未授权访问的情况的回调，框架配置中没有使用，而是使用遇到未授权访问直接注销返回到登录界面。
	 * 这里保留是为了，如果有需求可以配置框架返回到这里进行后续处理。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "onUnauthorized")
	public String onUnauthorized(HttpServletRequest request) {
		return "/manage/error/403";
	}

}
