/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-10-30 15:46 创建
 */
package com.acooly.module.olog.facade.order;

import com.acooly.core.common.facade.OrderBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/** @author acooly */
@Getter
@Setter
public class OlogOrder extends OrderBase {

  /** 所属系统标志 */
  @Length(max = 32)
  private String system;

  /** 模块名称 */
  @NotEmpty
  @Length(max = 255)
  private String moduleName;

  /** 功能模块 */
  @NotEmpty
  @Length(max = 255)
  private String module;

  /** 操作名称 */
  @NotEmpty
  @Length(max = 32)
  private String actionName;

  /** 操作 */
  @NotEmpty
  @Length(max = 32)
  private String action;

  /** 操作员 */
  @NotEmpty
  @Length(max = 64)
  private String operateUser;

  /** 操作员ID */
  private Long operateUserId;

  /** 操作结果.1:成功,2:失败 */
  private Boolean operateResult = Boolean.TRUE;

  @Length(max = 512)
  private String operateMessage;

  /** 备注 */
  @Length(max = 255)
  private String descn;

  /** 请求参数 */
  @Length(max = 512)
  private String requestParameters;

  /** 客户端信息 */
  @Length(max = 255)
  private String clientInformations;

  /** 执行时间 */
  private long executeMilliseconds = 0;
}
