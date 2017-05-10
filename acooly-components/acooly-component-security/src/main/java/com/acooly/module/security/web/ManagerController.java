package com.acooly.module.security.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.net.ServletUtil;
import com.acooly.core.utils.security.JWTUtils;
import com.acooly.module.security.config.FrameworkProperties;
import com.acooly.module.security.config.FrameworkPropertiesHolder;
import com.acooly.module.security.config.SecurityProperties;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.acooly.module.security.shiro.realm.ShiroDbRealm.SESSION_USER;

/**
 * 登录及主页
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author zhangpu
 */

@Controller
@RequestMapping(value = "/manage/")
@ConditionalOnProperty(value  = SecurityProperties.PREFIX + ".shiro.auth.enable",matchIfMissing = true)
public class ManagerController extends AbstractJQueryEntityController<User, UserService> {

	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Override
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
	    Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			// 如果已经登录的情况，直接回到主框架界面
			return "/manage/index";
		} else {
			// 如果没有登录的首次进入登录界面，直接返回到登录界面。
			FrameworkProperties frameworkProperties = FrameworkPropertiesHolder.get();
			// model.addAttribute("securityConfig", securityConfig);
			request.getSession(true).setAttribute("securityConfig", frameworkProperties);
			return "/manage/login";
		}
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
            /**
             * 如果已经登录的情况，其他系统集成sso则重定向目标地址，否则直接跳主页
             */
            String targetUrl = ServletUtil.getRequestParameter(JWTUtils.KEY_TARGETURL);
            //targetUrl = (String) ServletUtil.getSessionAttribute(JWTUtils.KEY_TARGETURL);
            if (StringUtils.isNotBlank(targetUrl)) {
                String jwt = JWTUtils.getJwtFromCookie(request.getCookies());
                return "redirect:" + fomartRederectUrl(targetUrl, jwt);
            }
            return "redirect:/manage/index.html";
        } else {
            // 如果没有登录的首次进入登录界面，直接返回到登录界面。
            FrameworkProperties frameworkProperties = FrameworkPropertiesHolder.get();
            // model.addAttribute("securityConfig", securityConfig);
            request.getSession(true).setAttribute("securityConfig", frameworkProperties);
            return "/manage/login";
        }
    }

    @RequestMapping(value = "onLoginSuccess")
    @ResponseBody
    public JsonResult onLoginSuccess(HttpServletRequest request) {
        logger.debug("OnLoginSuccess, redirect to index.jsp");
        JsonResult jsonResult = new JsonResult();
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute(SESSION_USER);
        if (user != null) {
            String username = user.getUsername();
            String subjectStr = "";
            try {
                subjectStr = JsonMapper.nonEmptyMapper().getMapper().writeValueAsString(user);
            } catch (JsonProcessingException e) {
                logger.error("创建jwt时user转String失败", e.getMessage());
            }
            String jwt = JWTUtils.createJwt(username,subjectStr);
            HashMap<Object, Object> resmap = Maps.newHashMap();

            JWTUtils.addJwtCookie(ServletUtil.getResponse(), jwt, JWTUtils.getDomainName());

            String targetUrl = (String) ServletUtil.getSessionAttribute(JWTUtils.KEY_TARGETURL);
            if (StringUtils.isNotBlank(targetUrl)) {
                resmap.put("isRedirect", true);
                resmap.put(JWTUtils.KEY_TARGETURL, fomartRederectUrl(targetUrl, jwt));
            } else {
                resmap.put("isRedirect", false);
                resmap.put(JWTUtils.KEY_TARGETURL, "");
            }
            jsonResult.setData(resmap);
        }
        jsonResult.setSuccess(true);
        return jsonResult;
    }

	private String fomartRederectUrl(String targetUrl,String jwt){
        if (targetUrl.contains("?")) {
            targetUrl = String.format("%s&jwt=%s", targetUrl, jwt);
        } else {
            targetUrl = String.format("%s?jwt=%s", targetUrl, jwt);
        }
        return targetUrl;
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
