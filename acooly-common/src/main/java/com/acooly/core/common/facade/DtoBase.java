/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:03 创建
 */
package com.acooly.core.common.facade;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author qiubo@yiji.com
 */
@Getter
@Setter
public class DtoBase extends InfoBase {
    private static final long serialVersionUID = 1L;
    /**
     * 商户订单号
     */
    private String merchOrderNo;
    /**
     * 业务订单号 *
     */
    @NotBlank
    private String bizOrderNo;
}
