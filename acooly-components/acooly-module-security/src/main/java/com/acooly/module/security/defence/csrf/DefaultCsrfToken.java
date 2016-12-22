/**
 * create by zhangpu
 * date:2015年3月1日
 */
package com.acooly.module.security.defence.csrf;

/**
 * @author zhangpu
 *
 */
@SuppressWarnings("serial")
public class DefaultCsrfToken implements CsrfToken {

	private String headerName;
	private String parameterName;
	private String token;

	public DefaultCsrfToken() {
	}

	public DefaultCsrfToken(String headerName, String parameterName, String token) {
		super();
		this.headerName = headerName;
		this.parameterName = parameterName;
		this.token = token;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
