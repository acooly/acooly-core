/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.service;

import java.io.Serializable;

import com.acooly.core.utils.enums.Messageable;

/**
 * @author zhangpu
 */
public interface Resultable extends Serializable {

	Messageable getStatus();

	String getDetail();

}
