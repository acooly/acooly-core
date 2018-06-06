/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.common.facade;

import com.acooly.core.utils.enums.Messageable;

import java.io.Serializable;

/**
 * @author zhangpu
 */
public interface Resultable extends Serializable {

    Messageable getStatus();

    String getDetail();
}
