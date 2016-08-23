package com.acooly.core.common.exception;

/**
 * 标记事务可回滚的业务异常,配合声明式事务使用
 * 
 * 业务系统可以根据业务需求，继承该类定义具体业务相关的业务。如：NoFoundException, ParameterInvaidException 等。
 * 
 * @author pu.zhang
 * 
 */
public class BusinessException extends AbstractI18NMessageException {

	/** UID */
	private static final long serialVersionUID = -9018571104185955115L;

	public BusinessException() {
		super();
	}

	public BusinessException(String errorCode, String[] errorArgs, String debugMessage, Throwable cause) {
		super(errorCode, errorArgs, debugMessage, cause);
	}

	public BusinessException(String errorCode, String[] errorArgs, Throwable cause) {
		super(errorCode, errorArgs, cause);
	}

	public BusinessException(String errorCode, String[] errorArgs) {
		super(errorCode, errorArgs);
	}

	public BusinessException(String debugMessage, Throwable cause) {
		super(debugMessage, cause);
	}

	public BusinessException(String debugMessage) {
		super(debugMessage);
	}

}
