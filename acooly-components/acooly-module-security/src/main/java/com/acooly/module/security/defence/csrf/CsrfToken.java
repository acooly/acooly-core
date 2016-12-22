/**
 * create by zhangpu
 * date:2015年3月1日
 */
package com.acooly.module.security.defence.csrf;

import java.io.Serializable;

/**
 * @author zhangpu
 *
 */
public interface CsrfToken extends Serializable {

	String getHeaderName();

	String getParameterName();

	String getToken();

}
