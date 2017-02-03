/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 11:47 创建
 */
package com.acooly.core.test.appservice;

import com.acooly.core.utils.service.OrderBase;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author qiubo@yiji.com
 */
@Data
public class TestOrder extends OrderBase {
    @Valid
    @NotNull
    private AppDto appDto;

}
