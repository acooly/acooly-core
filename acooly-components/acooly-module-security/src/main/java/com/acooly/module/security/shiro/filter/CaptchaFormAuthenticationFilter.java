package com.acooly.module.security.shiro.filter;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Strings;
import com.acooly.module.security.SecurityConstants;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.module.security.shiro.exception.InvaildCaptchaException;
import com.acooly.module.security.shiro.listener.ShireLoginLogoutSubject;
import com.google.common.collect.Maps;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 为Form-POST认证扩展Captcha
 * 
 * @author zhangpu
 * 
 */

public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);

	/** 界面请求的Input-form表单名称 */
	public String captchaInputName = "captcha";

	/** 登录失败Redirect URL */
	private String failureUrl = "/login.jsp";

	/** 监听处理 */
	private ShireLoginLogoutSubject shireLoginLogoutSubject;

	/** Captcha验证服务 */
	@Autowired
	protected ImageCaptchaService imageCaptchaService;

	@Autowired
	protected UserService userService;

	/**
	 * 扩展：在调用认证前，先验证验证码,同时，认证成功和失败都通过onLogin...方法直接Redirect到自定义的URL，进行后续日志，如日志拦截
	 * ，實現与安全控件解耦
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}
		try {

			User user = checkUserStatus(token, request);
			if (user.getLoginFailTimes() > 0) {
				checkCaptcha(request);
			}
			Subject subject = getSubject(request, response);
			subject.login(token);
			shireLoginLogoutSubject.afterLogin(token, null, (HttpServletRequest) request,
					(HttpServletResponse) response);
			return onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException e) {
			logger.debug("login failure. token:[" + token + "], exception:[" + e.getClass().getName() + "]");
			shireLoginLogoutSubject.afterLogin(token, e, (HttpServletRequest) request, (HttpServletResponse) response);
			return onLoginFailure(token, e, request, response);
		}
	}

	protected User checkUserStatus(AuthenticationToken token, ServletRequest request) {
		String username = (String) token.getPrincipal();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			logger.debug("login checkUserStatus：用户不存在");
			throw new UnknownAccountException("用户名或密码错误");
		}
		Date now = new Date();
		if (user.getStatus() == User.STATUS_LOCK) {
			if (now.getTime() >= user.getUnlockTime().getTime()) {
				logger.debug("用户已到解锁时间 {}，登录时自动解锁定", Dates.format(user.getUnlockTime()));
				user.setStatus(User.STATUS_ENABLE);
				user.setLastModifyTime(now);
				userService.save(user);
			} else {
				logger.debug("login checkUserStatus：用户已锁定:{}", user.getStatus());
				throw new AuthenticationException(
						"用户已锁定，解锁时间：" + Dates.format(user.getUnlockTime(), "yyyy-MM-dd HH:mm"));
			}
		}

		// 密码过期
		if (user.getStatus() == User.STATUS_EXPIRES || (SecurityConstants.USER_LOGIN_EXPIRE
				&& user.getExpirationTime() != null && now.getTime() >= user.getExpirationTime().getTime())) {
			user.setStatus(User.STATUS_EXPIRES);
			userService.save(user);
			logger.debug("密码已经过期, expireTime:{}", Dates.format(user.getExpirationTime()));
			throw new AuthenticationException("密码已过期，请联系管理员修改密码");
		}

		if (user.getStatus() != User.STATUS_ENABLE) {
			logger.debug("login checkUserStatus：用户状态非法:{}", user.getStatus());
			throw new AuthenticationException("用户已" + SecurityConstants.USER_STATUS_MAPPING.get(user.getStatus()));
		}
		return user;
	}

	/**
	 * 验证图片验证码
	 * 
	 * @param request
	 */
	protected void checkCaptcha(ServletRequest request) {
		String requestCaptcha = request.getParameter(captchaInputName);
		String captchaId = SecurityUtils.getSubject().getSession().getId().toString();
		Boolean captchaPassed = Boolean.TRUE;
		try {
			captchaPassed = imageCaptchaService.validateResponseForID(captchaId, requestCaptcha);
		} catch (CaptchaServiceException e) {
			logger.debug("Validate captcha, Exception -- > InvaildCaptchaException");
			throw new InvaildCaptchaException("captchaId format invaild.");
		}
		logger.debug("Validate captcha, captchaPassed -- > " + captchaPassed);
		if (!captchaPassed) {
			throw new InvaildCaptchaException("captcha invaild.");
		}
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		String username = (String) token.getPrincipal();
		userService.clearLoginFailureCount(username);
		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 扩展登录失败回调
	 * 
	 * 实现：失败后直接Redirect到指定的失败处理界面，如果Redirect失败，则使用框架的实现，直接返回请求界面直接处理。
	 * 
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		try {
			String username = (String) token.getPrincipal();
			User user = null;
			if (!UnknownAccountException.class.isAssignableFrom(e.getClass()) && Strings.isNotBlank(username)) {
				user = userService.addLoginFailureCount(username);
			}
			Map<String, String> queryParams = Maps.newHashMap();
			queryParams.put(getFailureKeyAttribute(), e.getClass().getName());
			queryParams.put("message", e.getMessage());
			int lastTimes = SecurityConstants.USER_LOGIN_LOCK_ERRORTIMES - user.getLoginFailTimes();
			if(lastTimes > 0){
				queryParams.put("lastTimes", String.valueOf(lastTimes));
			}
			WebUtils.issueRedirect(request, response, getFailureUrl(), queryParams, true);
			return true;
		} catch (Exception e2) {
			return super.onLoginFailure(token, e, request, response);
		}

	}

	/**
	 * 复写认证成功后，直接redirect到successUrl,不返回记录的上个请求地址。适应界面frame框架
	 */
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public void setShireLoginLogoutSubject(ShireLoginLogoutSubject shireLoginLogoutSubject) {
		this.shireLoginLogoutSubject = shireLoginLogoutSubject;
	}

}
