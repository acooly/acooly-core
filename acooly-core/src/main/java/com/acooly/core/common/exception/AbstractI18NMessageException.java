package com.acooly.core.common.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 异常抽象类,将错误信息国际化
 * 
 * @author pu.zhang
 */
public abstract class AbstractI18NMessageException extends RuntimeException {
	/** UID */
	private static final long serialVersionUID = 2148374270769534530L;

	private static final Logger logger = LoggerFactory.getLogger(AbstractI18NMessageException.class);

	/** 国际化支援文件 */
	private static final String ERROR_BUNDLE = "i18n/errors";

	/** 错误信息的i18n ResourceBundle. */
	static protected ResourceBundle rb;

	static {
		try {
			rb = ResourceBundle.getBundle(ERROR_BUNDLE, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.warn("加载国际化错误资源文件失败：{}", ERROR_BUNDLE);
		}
	}

	/** 错误代码,默认为未知错误 */
	private String errorCode = "UNKNOW_ERROR";

	/** 错误信息中的参数 */
	protected String[] errorArgs;

	/** 调试信息 */
	protected String debugMessage;

	public AbstractI18NMessageException() {
		this(null, null, null);
	}

	public AbstractI18NMessageException(String debugMessage) {
		this(null, null, debugMessage, null);
	}

	public AbstractI18NMessageException(String debugMessage, Throwable cause) {
		this(null, null, debugMessage, cause);
	}

	public AbstractI18NMessageException(String errorCode, String[] errorArgs) {
		this(errorCode, errorArgs, null, null);
	}

	public AbstractI18NMessageException(String errorCode, String[] errorArgs, Throwable cause) {
		this(errorCode, errorArgs, null, cause);
	}

	public AbstractI18NMessageException(String errorCode, String[] errorArgs, String debugMessage, Throwable cause) {
		super(debugMessage, cause);
		this.errorCode = errorCode;
		this.errorArgs = errorArgs;
	}

	public String getResourceMessage() {
		return getMessage();
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		try {
			if (rb != null && StringUtils.isNotBlank(errorCode)) {
				message = rb.getString(errorCode);
				// 将出错信息中的参数代入到出错信息。
				if (errorArgs != null) {
					message = MessageFormat.format(message, (Object[]) errorArgs);
				}
			}
		} catch (Exception mse) {
			logger.warn("获取错误消息失败, errorCode:{}", errorCode);
		}
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}