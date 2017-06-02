package com.acooly.core.common.exception;

import com.acooly.core.utils.enums.Messageable;

/**
 * 标记事务可回滚的业务异常,配合声明式事务使用
 *
 * <p>业务系统可以根据业务需求，继承该类定义具体业务相关的业务。如：NoFoundException, ParameterInvaidException 等。
 *
 * @author pu.zhang
 */
public class BusinessException extends RuntimeException implements Messageable {

  private static final long serialVersionUID = 1L;

  private String code = "SYSTEM";

  public BusinessException() {
    super();
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

  public BusinessException(String message, String code) {
    super(message);
    this.code = code;
  }

  public BusinessException(String message, Throwable cause, String code) {
    super(message, cause);
    this.code = code;
  }

  public BusinessException(Throwable cause, String code) {
    super(cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String code() {
    return this.code;
  }

  @Override
  public String message() {
    return this.getMessage();
  }
}
