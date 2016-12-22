/**
 * create by zhangpu
 * date:2015年3月1日
 */
package com.acooly.module.security.defence.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;

/**
 * @author zhangpu
 *
 */
public interface CsrfTokenRepository {

	 CsrfToken generateToken(HttpServletRequest request);
	 
	    void saveToken(CsrfToken token, HttpServletRequest request,
	            HttpServletResponse response);
	
}
