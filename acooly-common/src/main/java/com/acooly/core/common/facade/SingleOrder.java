/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:08 创建
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.Ids;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author qiubo@yiji.com
 */
@Setter
@Getter
@NoArgsConstructor
public class SingleOrder<T> extends OrderBase {
    @Valid
    @NotNull
    private T dto;

    public static <T> SingleOrder<T> from(T dto) {
        SingleOrder<T> order = new SingleOrder<T>();
        order.setDto(dto);
        return order;
    }

    public SingleOrder<T> gid() {
        return this.gid(Ids.gid());
    }

    public SingleOrder<T> gid(String gid) {
        this.setGid(gid);
        return this;
    }

    public SingleOrder<T> partnerId(String partnerId) {
        this.setPartnerId(partnerId);
        return this;
    }
}
