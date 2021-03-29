/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import java.io.Serializable;

/**
 * @author zhangpu
 */
public interface Orderable extends Serializable {

    /**
     * GID
     *
     * @return
     */
    String getGid();

    /**
     * 合作方ID
     *
     * @return
     */
    String getPartnerId();

    /**
     * 合法性检测
     */
    void check();


    /**
     * 租户ID
     *
     * @return
     */
    String getTenantId();
}
