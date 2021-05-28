/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.enums;

import java.io.Serializable;

/**
 * 统一枚举接口
 *
 * @author zhangpu
 */
public interface Messageable extends Serializable {

    /**
     * 编码
     *
     * @return
     */
    String code();

    /**
     * 消息
     *
     * @return
     */
    String message();
}
