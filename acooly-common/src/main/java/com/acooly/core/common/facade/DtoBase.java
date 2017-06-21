/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:03 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.ToString;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/** @author qiubo@yiji.com */
@Getter
@Setter
public class DtoBase implements Serializable {

  /** 商户订单号 */
  private String merchOrderNo;
  /** 业务订单号 * */
  @NotEmpty private String bizOrderNo;

  @Override
  public String toString() {
    return ToString.toString(this);
  }

  /** 请求参数校验,在使用@AppService并且没有启用校验组@AppService.ValidationGroup时会被调用 */
  public void check() throws OrderCheckException {}
}