package com.acooly.core.common.exception;

/**
 * 一般业务异常（非事务异常）
 * 
 * @author pu.zhang
 * 
 */
public class GeneralException extends AbstractI18NMessageException {

	/** serialVersionUID */
	private static final long serialVersionUID = -6910756266634262775L;

	public GeneralException() {
		super();
	}

	public GeneralException(String errorCode, String[] errorArgs, String debugMessage, Throwable cause) {
		super(errorCode, errorArgs, debugMessage, cause);
		// TODO Auto-generated constructor stub
	}

	public GeneralException(String errorCode, String[] errorArgs, Throwable cause) {
		super(errorCode, errorArgs, cause);
		// TODO Auto-generated constructor stub
	}

	public GeneralException(String errorCode, String[] errorArgs) {
		super(errorCode, errorArgs);
		// TODO Auto-generated constructor stub
	}

	public GeneralException(String debugMessage, Throwable cause) {
		super(debugMessage, cause);
		// TODO Auto-generated constructor stub
	}

	public GeneralException(String debugMessage) {
		super(debugMessage);
		// TODO Auto-generated constructor stub
	}

}
