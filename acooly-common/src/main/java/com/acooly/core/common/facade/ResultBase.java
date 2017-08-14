/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.ResultStatus;

/** @author zhangpu */
public class ResultBase extends LinkedHashMapParameterize<String, Object>
    implements Resultable, Messageable {

  /** serialVersionUID */
  private static final long serialVersionUID = -8702480923545642017L;

  private ResultStatus status = ResultStatus.success;

  /** 参考 {@link ResultCode} */
  private String code = ResultCode.SUCCESS.getCode();

  private String detail=ResultCode.SUCCESS.getMessage();

  public Messageable getStatus() {
    return status;
  }

  public void setStatus(ResultStatus status) {
    this.status = status;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean success() {
    return status == ResultStatus.success;
  }

  public boolean processing() {
      return status == ResultStatus.processing;
  }

    @Override
  public String toString() {
    return ToString.toString(this);
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String message() {
    return detail;
  }

    /**
     * 当statu != ResultStatus.success抛出业务异常
     */
  public void throwExceptionIfNotSuccess(){
      if(!success()){
          throw new BusinessException(this.getCode(), this.getDetail());
      }
  }
}
