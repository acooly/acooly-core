package com.acooly.module.security.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.module.security.config.SecurityConfig;
import com.acooly.module.security.config.SecurityConfigHolder;

/**
 * 系统框架参数
 * 
 * @author zhangpu
 */

@Controller
@RequestMapping(value = "/security/config")
public class SecurityConfigController {

	/**
	 * 授权功能顶级菜单
	 * 
	 * @return
	 */
	@RequestMapping("index")
	@ResponseBody
	public SecurityConfig authorisedMenus(HttpServletRequest request, HttpServletResponse response) {
		return SecurityConfigHolder.getSecurityConfig();
	}

}
