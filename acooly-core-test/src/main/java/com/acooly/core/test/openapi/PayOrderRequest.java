/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 22:04 创建
 */
package com.acooly.core.test.openapi;

import com.acooly.core.utils.Money;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiRequest;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author qiubo@yiji.com
 */
@OpenApiMessage(service = "payOrder", type = ApiMessageType.Request)
public class PayOrderRequest extends ApiRequest {

    @NotNull
    @OpenApiField(desc = "金额")
    private Money amount;
    @NotEmpty
    @OpenApiField(desc = "买家用户ID")
    private String payerUserId;

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(String payerUserId) {
        this.payerUserId = payerUserId;
    }

}
